plugins {
    kotlin("jvm")
    id("com.github.gmazzo.buildconfig")
    idea
    id("maven-publish")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val annotationsRuntimeClasspath: Configuration by configurations.creating { isTransitive = false }

dependencies {
    compileOnly(kotlin("compiler"))
    annotationsRuntimeClasspath(project(":is-annotations"))
    compileOnly(project(":is-annotations"))
    compileOnly(kotlin("stdlib"))
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
}

buildConfig {
    useKotlinOutput {
        internalVisibility = true
    }
    packageName(group.toString())
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${rootProject.group}\"")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    archiveClassifier = ""
    exclude("kotlin/**")
    exclude("org/**")
    mergeServiceFiles()
}