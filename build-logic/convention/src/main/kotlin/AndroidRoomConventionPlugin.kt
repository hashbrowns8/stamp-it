
import androidx.room.gradle.RoomExtension
import it.stamp.library
import it.stamp.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("androidx.room")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                "implementation"(libs.library("room-runtime"))
                "implementation"(libs.library("room-ktx"))
                "ksp"(libs.library("room-compiler"))
            }
        }
    }
}