package it.stamp

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

internal fun Project.configureKotlin() {
    kotlinExtension.jvmToolchain(17)
}