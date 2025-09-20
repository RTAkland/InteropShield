plugins {
    kotlin("multiplatform")
    id("cn.rtast.interop") version "1.0.0-2.2.10"
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

tasks.withType<AbstractPublishToMaven>().configureEach {
    enabled = false
}

interopShield {
    enabled = true
    suppressWarning = false
}
