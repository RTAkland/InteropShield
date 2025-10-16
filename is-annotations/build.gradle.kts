plugins {
    kotlin("multiplatform")
    id("maven-publish")
}

kotlin {
    jvm()
    macosArm64()
    linuxX64()
    linuxArm64()
    mingwX64()
    iosArm64()
    macosX64()
}