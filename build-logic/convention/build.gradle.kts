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
    compileOnly(libs.gradle.plugin.room)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("android-application") {
            id = libs.plugins.stampit.android.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("android-library") {
            id = libs.plugins.stampit.android.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("android-feature") {
            id = libs.plugins.stampit.android.feature.get().pluginId
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("android-compose") {
            id = libs.plugins.stampit.android.compose.get().pluginId
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("android-hilt") {
            id = libs.plugins.stampit.android.hilt.get().pluginId
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("jvm-library") {
            id = libs.plugins.stampit.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("android-room") {
            id = libs.plugins.stampit.android.room.get().pluginId
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
}