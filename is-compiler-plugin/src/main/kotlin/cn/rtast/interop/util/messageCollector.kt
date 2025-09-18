/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/14/25
 */


package cn.rtast.interop.util

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector

fun MessageCollector.report(vararg message: Any?) =
    this.report(CompilerMessageSeverity.WARNING, message.joinToString { it.toString() })