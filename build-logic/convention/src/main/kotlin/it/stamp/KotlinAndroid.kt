package it.stamp

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) = with(commonExtension) {
    configureKotlin()

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 29
    }
}