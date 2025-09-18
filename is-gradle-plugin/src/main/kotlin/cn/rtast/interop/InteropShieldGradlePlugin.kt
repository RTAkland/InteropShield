/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/13/25
 */

@file:Suppress("unused")

package cn.rtast.interop

import cn.rtast.interop.BuildConfig.ANNOTATIONS_LIBRARY_COORDINATES
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class InteropShieldGradlePlugin : KotlinCompilerPluginSupportPlugin {
    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        val extension = kotlinCompilation.target.project.extensions.findByType(InteropShieldExtension::class.java)
            ?: InteropShieldExtension()
        return project.provider {
            listOf(
                SubpluginOption("enabled", extension.enabled.toString()),
                SubpluginOption("suppressWarning", extension.suppressWarning.toString())
            )
        }
    }

    override fun apply(target: Project) {
        target.extensions.create("interopShield", InteropShieldExtension::class.java)
        target.plugins.withId("org.jetbrains.kotlin.multiplatform") {
            val kotlinExt = target.extensions.getByName("kotlin") as KotlinMultiplatformExtension
            val commonMain = kotlinExt.sourceSets.getByName("commonMain")
            target.dependencies.add(
                commonMain.apiConfigurationName,
                ANNOTATIONS_LIBRARY_COORDINATES
            )
        }
        super.apply(target)
    }

    override fun getCompilerPluginId(): String = BuildConfig.KOTLIN_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = BuildConfig.KOTLIN_PLUGIN_GROUP,
        artifactId = BuildConfig.KOTLIN_PLUGIN_NAME,
        version = BuildConfig.KOTLIN_PLUGIN_VERSION,
    )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true
}