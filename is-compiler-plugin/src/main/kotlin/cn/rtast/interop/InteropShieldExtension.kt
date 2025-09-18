/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/14/25
 */


package cn.rtast.interop

import cn.rtast.interop.ir.InteropShieldIrTransformer
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

class InteropShieldExtension(
    private val messageCollector: MessageCollector,
    private val configuration: CompilerConfiguration,
) : IrGenerationExtension {
    override fun generate(
        moduleFragment: IrModuleFragment,
        pluginContext: IrPluginContext,
    ) {
        moduleFragment.transform(
            InteropShieldIrTransformer(
                messageCollector,
                pluginContext,
                configuration
            ), null
        )
    }
}