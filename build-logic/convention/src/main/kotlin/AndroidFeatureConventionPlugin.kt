
import it.stamp.library
import it.stamp.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("stampit.android.library")
                apply("stampit.android.compose")
                apply("stampit.android.hilt")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                "implementation"(project(":core:domain"))
                "implementation"(project(":core:designsystem"))
                "implementation"(project(":core:ui"))
                "implementation"(platform(libs.library("compose-bom")))
                "implementation"(libs.library("compose-ui"))
                "implementation"(libs.library("compose-ui-graphics"))
                "implementation"(libs.library("compose-ui-tooling-preview"))
                "implementation"(libs.library("compose-material3"))
                "implementation"(libs.library("androidx-lifecycle-runtime-compose"))
                "implementation"(libs.library("androidx-hilt-lifecycle-viewmodel-compose"))
                "implementation"(libs.library("androidx-navigation3-runtime"))
                "implementation"(libs.library("coil-compose"))
                "implementation"(libs.library("coil-network-okhttp"))
                "implementation"(libs.library("kotlinx-serialization-json"))
                "implementation"(libs.library("kotlinx-collections-immutable"))
                "implementation"(libs.library("timber"))
                "testImplementation"(libs.library("junit"))
                "androidTestImplementation"(libs.library("androidx-junit"))
                "androidTestImplementation"(libs.library("androidx-espresso-core"))
                "androidTestImplementation"(platform(libs.library("compose-bom")))
                "androidTestImplementation"(libs.library("compose-ui-test-junit4"))
                "debugImplementation"(libs.library("compose-ui-tooling"))
                "debugImplementation"(libs.library("compose-ui-test-manifest"))
            }
        }
    }
}