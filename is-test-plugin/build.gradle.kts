plugins {
    kotlin("multiplatform")
    id("cn.rtast.interop") version "0.0.1-2.2.10"
}

repositories {
    mavenCentral()
//    maven("https://repo.maven.rtast.cn/releases")
}

kotlin {
    jvm()

    sourceSets {
        jvmMain.dependencies {

        }
    }
}

interopShield {
    enabled = true
    suppressWarning = false
}
