
import com.android.build.api.dsl.LibraryExtension
import it.stamp.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
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

            extensions.configure<LibraryExtension> {
                dependencies {
                    "implementation"(project(":core:domain"))
                    "implementation"(project(":core:ui"))
                    "implementation"(project(":core:designsystem"))

                    "implementation"(findLibrary("androidx-lifecycle-runtime-compose"))
                    "implementation"(findLibrary("androidx-hilt-lifecycle-viewmodel-compose"))
                    "implementation"(findLibrary("androidx-navigation3-runtime"))
                    "implementation"(findLibrary("kotlinx-serialization-json"))
                }
            }
        }
    }
}