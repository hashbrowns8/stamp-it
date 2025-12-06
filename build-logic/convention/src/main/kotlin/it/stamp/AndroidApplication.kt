package it.stamp

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

internal fun Project.configureAndroidApplication(
    applicationExtension: ApplicationExtension
) = with(applicationExtension) {
    configureKotlinAndroid(this)

    namespace = "it.stamp"

    defaultConfig {
        applicationId = "it.stamp"
        targetSdk = 36
    }
}