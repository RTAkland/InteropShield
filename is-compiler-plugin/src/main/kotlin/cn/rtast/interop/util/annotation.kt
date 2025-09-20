/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/15/25
 */


@file:OptIn(
    UnsafeDuringIrConstructionAPI::class, FirIncompatiblePluginAPI::class,
    DeprecatedForRemovalCompilerApi::class
)

package cn.rtast.interop.util

import org.jetbrains.kotlin.DeprecatedForRemovalCompilerApi
import org.jetbrains.kotlin.backend.common.extensions.FirIncompatiblePluginAPI
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.fromSymbolOwner
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.getAnnotationStringValue
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.FqName

val AUTO_GEN_GETTER_FQ_NAME = "cn.rtast.interop.annotation.AutoGenGetter".fqName
val AUTO_GEN_SETTER_FQ_NAME = "cn.rtast.interop.annotation.AutoGenSetter".fqName
val JAVA_ONLY_FQ_NAME = "cn.rtast.interop.annotation.JavaOnly".fqName

fun IrProperty.hasAnnotationByFqName(fqName: FqName): Boolean =
    this.annotations.any { it.symbol.owner.parentAsClass.fqNameWhenAvailable == fqName }

fun IrProperty.hasAutoGenGetter(): Boolean =
    this.hasAnnotationByFqName(AUTO_GEN_GETTER_FQ_NAME)

fun IrProperty.hasAutoGenSetter(): Boolean =
    this.hasAnnotationByFqName(AUTO_GEN_SETTER_FQ_NAME)

fun IrPluginContext.annotationConstructorCall(
    annotationFqName: FqName,
    targetFunction: IrSimpleFunction,
): IrConstructorCallImpl? {
    val annotationClass = this.referenceClass(annotationFqName.classId)
    val constructor = annotationClass?.constructors?.firstOrNull() ?: return null
    val irAnnotation = IrConstructorCallImpl.fromSymbolOwner(
        targetFunction.startOffset,
        targetFunction.endOffset,
        annotationClass.defaultType,
        constructor
    )
    return irAnnotation
}

fun IrProperty.getAnnotationArgument(annotationFqName: FqName, parameterName: String = "functionName"): String {
    val anno = this.annotations.firstOrNull {
        it.symbol.owner.parentAsClass.fqNameWhenAvailable == annotationFqName
    }
    return anno?.getAnnotationStringValue() ?: "@"
}

fun IrProperty.getGetterFuncName(): String = this.getAnnotationArgument(AUTO_GEN_GETTER_FQ_NAME)
fun IrProperty.getSetterFuncName(): String = this.getAnnotationArgument(AUTO_GEN_SETTER_FQ_NAME)