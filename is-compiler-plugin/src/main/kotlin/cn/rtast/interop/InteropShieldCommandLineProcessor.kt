/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/19/25
 */


@file:OptIn(ExperimentalCompilerApi::class)
@file:Suppress("unused")

package cn.rtast.interop

import cn.rtast.interop.cfg.ENABLED
import cn.rtast.interop.cfg.SUPPRESS_WARNING
import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(CommandLineProcessor::class)
class InteropShieldCommandLineProcessor: CommandLineProcessor {
    override val pluginId: String = "cn.rtast.interop"
    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption(
            optionName = "enabled",
            valueDescription = "true or false",
            required = true,
            allowMultipleOccurrences = false,
            description = "是否启用"
        ),
        CliOption(
            optionName = "suppressWarning",
            valueDescription = "true or false",
            required = true,
            allowMultipleOccurrences = false,
            description = "是否禁用警告"
        ),
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option.optionName) {
            "enabled" -> configuration.put(ENABLED, value.toBoolean())
            "suppressWarning" -> configuration.put(SUPPRESS_WARNING, value.toBoolean())
        }
    }
}