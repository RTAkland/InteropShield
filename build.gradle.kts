plugins {
    kotlin("jvm") version "2.2.20" apply false
    kotlin("multiplatform") version "2.2.20" apply false
    id("com.github.gmazzo.buildconfig") version "5.6.5"
    id("maven-publish")
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    group = "cn.rtast.interop"
    version = "1.0.2-2.2.20"
}

subprojects {
    apply(plugin = "maven-publish")

    publishing {
        repositories {
//            mavenLocal()
            maven("https://repo.maven.rtast.cn/releases") {
                credentials {
                    username = "RTAkland"
                    password = System.getenv("PUBLISH_TOKEN")
                }
            }
        }
    }
}
