package it.stamp

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) = with(commonExtension) {
    buildFeatures {
        compose = true
    }

    configure<KotlinAndroidProjectExtension> {
        compilerOptions {
            optIn.addAll("androidx.compose.material3.ExperimentalMaterial3Api")
        }
    }

    dependencies {
        "implementation"(platform(libs.library("compose-bom")))
        "androidTestImplementation"(platform(libs.library("compose-bom")))
        "implementation"(libs.library("compose-ui-tooling-preview"))
        "debugImplementation"(libs.library("compose-ui-tooling"))
    }
}