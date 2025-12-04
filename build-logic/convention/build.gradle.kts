plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(17)
}

group = "it.stamp.buildlogic"

dependencies {
    compileOnly(libs.gradle.plugin.android)
    compileOnly(libs.gradle.plugin.kotlin)
    compileOnly(libs.gradle.plugin.ksp)
    compileOnly(libs.gradle.plugin.compose.compiler)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}