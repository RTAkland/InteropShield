/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/21/25
 */


package cn.rtast.interop.util

import cn.rtast.interop.ir.InteropShieldIrTransformer
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.name.Name

fun String.toName(): Name = Name.identifier(this)

fun getDefaultGeneratedFunctionName(
    genType: InteropShieldIrTransformer.MemberType,
    declaration: IrProperty,
): Name {
    return "${genType.prefix}${declaration.name.asString().replaceFirstChar { it.uppercase() }}".toName()
}