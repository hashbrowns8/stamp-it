
import com.android.build.api.dsl.LibraryExtension
import it.stamp.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("stampit.android.library")
                apply("stampit.android.hilt")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            val extension = extensions.getByType<LibraryExtension>()

            extension.apply {
                dependencies {
                    "implementation"(findLibrary("kotlinx-serialization-json"))
                }
            }
        }
    }
}