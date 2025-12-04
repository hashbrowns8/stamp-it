package it.stamp

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) = with(commonExtension) {
    buildFeatures {
        compose = true
    }

    dependencies {
        "implementation"(platform(findLibrary("compose-bom")))
        "androidTestImplementation"(platform(findLibrary("compose-bom")))
        "implementation"(findLibrary("compose-ui-tooling-preview"))
        "debugImplementation"(findLibrary("compose-ui-tooling"))
    }
}