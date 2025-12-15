
import com.android.build.api.dsl.ApplicationExtension
import it.stamp.configureAndroidApplication
import it.stamp.library
import it.stamp.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension>(::configureAndroidApplication)

            dependencies {
                "implementation"(libs.library("timber"))
            }
        }
    }
}