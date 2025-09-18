/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/13/25
 */


@file:OptIn(IrImplementationDetail::class, UnsafeDuringIrConstructionAPI::class, ObsoleteDescriptorBasedAPI::class)

package cn.rtast.interop.ir

import cn.rtast.interop.cfg.SUPPRESS_WARNING
import cn.rtast.interop.util.*
import org.jetbrains.kotlin.DeprecatedForRemovalCompilerApi
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrImplementationDetail
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.builders.declarations.addFunction
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrTypeParameter
import org.jetbrains.kotlin.ir.descriptors.IrBasedClassDescriptor
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.copyTo
import org.jetbrains.kotlin.name.Name

@OptIn(DeprecatedForRemovalCompilerApi::class)
class InteropShieldIrTransformer(
    private val messageCollector: MessageCollector,
    private val pluginContext: IrPluginContext,
    private val configuration: CompilerConfiguration,
) : IrElementTransformerVoidWithContext() {

    @Suppress("unused")
    private val allowedClassKind = listOf(
        ClassKind.CLASS,
        ClassKind.OBJECT,
        ClassKind.ENUM_CLASS

    )

    private val warningClassKind = listOf(
        ClassKind.INTERFACE,
    )

    private val disallowedClassKind = listOf(
        ClassKind.ANNOTATION_CLASS
    )

    enum class MemberType(val prefix: String) {
        Getter("get"), Setter("set")
    }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (!declaration.hasAutoGenGetter() && !declaration.hasAutoGenSetter()) return super.visitPropertyNew(
            declaration
        )
        val receiverExtension = declaration.descriptor.extensionReceiverParameter
        if (receiverExtension != null) {
            val receiverClass =
                (receiverExtension.type.constructor.declarationDescriptor as? IrBasedClassDescriptor)?.owner
            if (receiverClass?.kind in disallowedClassKind) {
                val explicitKind = disallowedClassKind.find { it == receiverClass?.kind }
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "不允许使用插件向 ${explicitKind?.codeRepresentation} 类中生成getter/setter"
                )
            }
            if (receiverClass?.kind in warningClassKind && !configuration.get(SUPPRESS_WARNING)!!) {
                val explicitKind = warningClassKind.find { it == receiverClass?.kind }
                messageCollector.report("正在向 ${explicitKind?.codeRepresentation} 类中生成getter/setter, 可能会影响Java使用者体验")
            }
            receiverClass?.generateFunction(MemberType.Getter, declaration, receiverClass)
            if (declaration.setter == null) {
                return super.visitPropertyNew(declaration)
            } else {
                if (declaration.hasAutoGenSetter()) receiverClass?.generateFunction(
                    MemberType.Setter,
                    declaration,
                    receiverClass
                ) else return super.visitPropertyNew(declaration)
            }

        }
        return super.visitPropertyNew(declaration)
    }

    private fun IrClass?.generateFunction(genType: MemberType, declaration: IrProperty, receiverClass: IrClass) {
        val originIrSimpleFunction = when (genType) {
            MemberType.Getter -> declaration.getter
            MemberType.Setter -> declaration.setter
        } ?: return
        val newFunc = this?.addFunction {
            startOffset = declaration.startOffset
            endOffset = declaration.endOffset
            origin = IrDeclarationOrigin.DEFINED
            name =
                Name.identifier("${genType.prefix}${declaration.name.asString().replaceFirstChar { it.uppercase() }}")
            visibility = declaration.visibility
            modality = Modality.OPEN
            returnType = originIrSimpleFunction.returnType
        }?.apply addFunction@{
            parent = this@generateFunction
            // 设置参数
            valueParameters = if (genType == MemberType.Getter) listOf() else
                originIrSimpleFunction.valueParameters.map { it.copyTo(this) }
            if (originIrSimpleFunction.typeParameters.isEmpty()) {
                dispatchReceiverParameter = receiverClass.thisReceiver?.copyTo(this)
            } else {
                val newIrTypeParameters = mutableListOf<IrTypeParameter>()
                for (param in originIrSimpleFunction.typeParameters) {
                    newIrTypeParameters.add(param)
                }
                typeParameters = newIrTypeParameters
                dispatchReceiverParameter = receiverClass.thisReceiver?.copyTo(this)
            }
        }

        newFunc?.let {
            it.body = DeclarationIrBuilder(pluginContext, it.symbol, startOffset, endOffset).irBlockBody {
                val call = irCall(originIrSimpleFunction.symbol).apply irCall@{
                    extensionReceiver = irGet(it.dispatchReceiverParameter!!)
                    for (i in originIrSimpleFunction.typeParameters.indices) {
                        putTypeArgument(i, getTypeArgument(i))
                    }
                    // 设置参数
                    if (genType == MemberType.Setter) {
                        originIrSimpleFunction.valueParameters.forEachIndexed { index, _ ->
                            putValueArgument(index, irGet(it.valueParameters[index]))
                        }
                    }
                }
                +irReturn(call)
            }
            val annotationCall = pluginContext.annotationConstructorCall(JAVA_ONLY_FQ_NAME, it)!!
            it.annotations += annotationCall
        }
    }
}
