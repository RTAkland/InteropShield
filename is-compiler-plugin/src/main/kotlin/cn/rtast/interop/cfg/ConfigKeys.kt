/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/19/25
 */


package cn.rtast.interop.cfg

import org.jetbrains.kotlin.config.CompilerConfigurationKey

fun <T> String.toCompilerConfigKey(): CompilerConfigurationKey<T> =
    CompilerConfigurationKey(this)

val ENABLED = "enabled".toCompilerConfigKey<Boolean>()
val SUPPRESS_WARNING = "suppressWarning".toCompilerConfigKey<Boolean>()