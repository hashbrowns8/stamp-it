import it.stamp.library
import it.stamp.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("stampit.android.library")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                "api"(libs.library("androidx-navigation3-runtime"))
            }
        }
    }
}