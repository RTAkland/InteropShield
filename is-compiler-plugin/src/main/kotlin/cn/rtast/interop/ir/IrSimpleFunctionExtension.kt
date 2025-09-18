/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/13/25
 */


package cn.rtast.interop.ir

import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

fun IrSimpleFunction?.isExtension(): Boolean {
    return this?.getReceiver() != null
}

fun IrSimpleFunction?.getReceiver() = this?.parameters?.firstOrNull { it.kind == IrParameterKind.ExtensionReceiver }