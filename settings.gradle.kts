@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
//        maven("https://repo.maven.rtast.cn/releases")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "InteropShield"

include(":is-gradle-plugin")
include(":is-compiler-plugin")
include(":is-annotations")
include(":is-test-plugin")