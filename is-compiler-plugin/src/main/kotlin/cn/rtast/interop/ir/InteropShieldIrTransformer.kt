/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/13/25
 */


@file:OptIn(IrImplementationDetail::class, UnsafeDuringIrConstructionAPI::class, ObsoleteDescriptorBasedAPI::class,
    DelicateIrParameterIndexSetter::class
)

package cn.rtast.interop.ir

import cn.rtast.interop.cfg.SUPPRESS_WARNING
import cn.rtast.interop.util.*
import org.jetbrains.kotlin.DeprecatedForRemovalCompilerApi
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrImplementationDetail
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.addFunction
import org.jetbrains.kotlin.ir.declarations.DelicateIrParameterIndexSetter
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.createBlockBody
import org.jetbrains.kotlin.ir.declarations.impl.IrValueParameterImpl
import org.jetbrains.kotlin.ir.descriptors.IrBasedClassDescriptor
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrReturnImpl
import org.jetbrains.kotlin.ir.expressions.impl.fromSymbolOwner
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.defaultType
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
            receiverClass?.generateFunction(MemberType.Getter, declaration, declaration.getGetterFuncName())
            if (declaration.setter == null) {
                return super.visitPropertyNew(declaration)
            } else {
                if (declaration.hasAutoGenSetter()) receiverClass?.generateFunction(
                    MemberType.Setter,
                    declaration, declaration.getSetterFuncName()
                ) else return super.visitPropertyNew(declaration)
            }

        }
        return super.visitPropertyNew(declaration)
    }

    private fun IrClass?.generateFunction(
        genType: MemberType,
        declaration: IrProperty,
        generatedFunctionName: String,
    ) {
        val originIrSimpleFunction = when (genType) {
            MemberType.Getter -> declaration.getter
            MemberType.Setter -> declaration.setter
        } ?: return
        val newFuncName =
            if (generatedFunctionName == "@") getDefaultGeneratedFunctionName(genType, declaration)
            else generatedFunctionName.toName()
        this?.addFunction {
            startOffset = declaration.startOffset
            endOffset = declaration.endOffset
            origin = IrDeclarationOrigin.DEFINED
            name = newFuncName
            visibility = declaration.visibility
            modality = Modality.OPEN
            returnType = originIrSimpleFunction.returnType
        }!!.apply addFunction@{
            parent = this@generateFunction
            dispatchReceiverParameter = IrValueParameterImpl(
                UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                IrDeclarationOrigin.INSTANCE_RECEIVER, pluginContext.irFactory,
                name = Name.identifier("this"),
                type = this@generateFunction.defaultType,
                isAssignable = false,
                symbol = IrValueParameterSymbolImpl(),
                varargElementType = null,
                isCrossinline = false,
                isNoinline = false,
                isHidden = false
            ).apply dispatcher@{
                this@dispatcher.parent = this@addFunction
            }

            val irCall = IrCallImpl.fromSymbolOwner(
                UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                symbol = originIrSimpleFunction.symbol,
            ).apply irCall@{
                this@irCall.typeArguments
                if (genType == MemberType.Setter) {
                    val valueParam = IrValueParameterImpl(
                        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                        IrDeclarationOrigin.DEFINED, pluginContext.irFactory,
                        name = Name.identifier("value"),
                        type = originIrSimpleFunction.valueParameters.first().type,
                        isAssignable = true,
                        symbol = IrValueParameterSymbolImpl(),
                        varargElementType = null,
                        isCrossinline = false,
                        isNoinline = false,
                        isHidden = false
                    ).apply {
                        index = 0
                        this.parent = this@addFunction
                    }
                    valueParameters = listOf(valueParam)
                    putValueArgument(0, IrGetValueImpl(
                        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                        valueParam.type,
                        valueParam.symbol
                    ))
                }
            }
            // new
            typeParameters = originIrSimpleFunction.typeParameters
            originIrSimpleFunction.typeParameters.forEachIndexed { index, tp ->
                irCall.putTypeArgument(index, tp.defaultType)
            }
            // end new
            val thisExpr = IrGetValueImpl(
                UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                originIrSimpleFunction.returnType,
                this@addFunction.dispatchReceiverParameter!!.symbol,
            )
            irCall.extensionReceiver = thisExpr
            val returnStatement = IrReturnImpl(
                UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                pluginContext.irBuiltIns.nothingType,
                this@addFunction.symbol,
                irCall
            )
            body = pluginContext.irFactory.createBlockBody(UNDEFINED_OFFSET, UNDEFINED_OFFSET, listOf(returnStatement))
        }
    }
}
