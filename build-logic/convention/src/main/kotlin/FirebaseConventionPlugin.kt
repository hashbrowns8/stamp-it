
import it.stamp.library
import it.stamp.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.gms.google-services")

            dependencies {
                "implementation"(platform(libs.library("firebase-bom")))
            }
        }
    }
}