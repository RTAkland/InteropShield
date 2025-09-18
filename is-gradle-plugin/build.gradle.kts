plugins {
    kotlin("jvm")
    id("com.github.gmazzo.buildconfig")
    id("java-gradle-plugin")
    id("maven-publish")
}

dependencies {
    implementation(kotlin("gradle-plugin-api"))
    implementation(project(":is-annotations"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20")
}

buildConfig {
    packageName(project.group.toString())
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${rootProject.group}\"")
    val pluginProject = project(":is-compiler-plugin")
    buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${pluginProject.group}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"${pluginProject.name}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${pluginProject.version}\"")

    val annotationsProject = project(":is-annotations")
    buildConfigField(
        type = "String",
        name = "ANNOTATIONS_LIBRARY_COORDINATES",
        expression = "\"${annotationsProject.group}:${annotationsProject.name}:${annotationsProject.version}\""
    )
}

gradlePlugin {
    plugins {
        create("InteropShield") {
            id = rootProject.group.toString()
            displayName = "InteropShield"
            description = "InteropShield"
            implementationClass = "cn.rtast.interop.InteropShieldGradlePlugin"
        }
    }
}