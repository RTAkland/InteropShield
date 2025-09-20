/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/19/25
 */


package cn.rtast.interop.util

import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrTypeParameter

fun IrSimpleFunction.hasGeneric(): Boolean = this.typeParameters.isEmpty()

fun IrSimpleFunction.copyTypeParametersTo(target: IrSimpleFunction) {
    val newIrTypeParameters = mutableListOf<IrTypeParameter>()
    for (param in this.typeParameters) {
        newIrTypeParameters.add(param)
    }
    target.typeParameters = newIrTypeParameters
}