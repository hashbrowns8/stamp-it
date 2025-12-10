
import com.android.build.api.dsl.LibraryExtension
import it.stamp.configureAndroidLibrary
import it.stamp.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension>(::configureAndroidLibrary)

            dependencies {
                "implementation"(findLibrary("timber"))
            }
        }
    }
}