
import it.stamp.configureKotlin
import it.stamp.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

class KotlinJvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            configureKotlin()

            kotlinExtension.apply {
                dependencies {
                    "implementation"(findLibrary("kotlinx-datetime"))
                    "implementation"(findLibrary("kotlinx-serialization-json"))
                    "implementation"(findLibrary("coroutines-core"))
                    "testImplementation"(findLibrary("coroutines-test"))
                }
            }
        }
    }
}