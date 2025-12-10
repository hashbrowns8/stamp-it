
import it.stamp.findLibrary
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
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:designsystem"))
                "implementation"(platform(findLibrary("compose-bom")))
                "implementation"(findLibrary("compose-ui"))
                "implementation"(findLibrary("compose-ui-graphics"))
                "implementation"(findLibrary("compose-ui-tooling-preview"))
                "implementation"(findLibrary("compose-material3"))
                "implementation"(findLibrary("androidx-lifecycle-runtime-compose"))
                "implementation"(findLibrary("androidx-hilt-lifecycle-viewmodel-compose"))
                "implementation"(findLibrary("androidx-navigation3-runtime"))
                "implementation"(findLibrary("kotlinx-serialization-json"))
                "implementation"(findLibrary("timber"))
                "testImplementation"(findLibrary("junit"))
                "androidTestImplementation"(findLibrary("androidx-junit"))
                "androidTestImplementation"(findLibrary("androidx-espresso-core"))
                "androidTestImplementation"(platform(findLibrary("compose-bom")))
                "androidTestImplementation"(findLibrary("compose-ui-test-junit4"))
                "debugImplementation"(findLibrary("compose-ui-tooling"))
                "debugImplementation"(findLibrary("compose-ui-test-manifest"))
            }
        }
    }
}