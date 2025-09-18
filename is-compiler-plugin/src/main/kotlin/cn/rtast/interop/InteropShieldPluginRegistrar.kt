/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/13/25
 */


@file:OptIn(ExperimentalCompilerApi::class)
@file:Suppress("unused")

package cn.rtast.interop

import cn.rtast.interop.cfg.ENABLED
import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(CompilerPluginRegistrar::class)
class InteropShieldPluginRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(
        configuration: CompilerConfiguration,
    ) {
        if (!configuration.get(ENABLED)!!) return
        val messageCollector = configuration.get(CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
        IrGenerationExtension.registerExtension(InteropShieldExtension(messageCollector, configuration))
    }
}