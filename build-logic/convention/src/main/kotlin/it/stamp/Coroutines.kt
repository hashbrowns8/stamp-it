package it.stamp

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureCoroutinesAndroid() {
    configureCoroutinesKotlin()
    dependencies {
        "implementation"(libs.findLibrary("coroutines-android").get())
    }
}

internal fun Project.configureCoroutinesKotlin() {
    dependencies {
        "implementation"(libs.findLibrary("coroutines-core").get())
        "testImplementation"(libs.findLibrary("coroutines-test").get())
    }
}