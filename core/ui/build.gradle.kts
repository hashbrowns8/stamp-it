plugins {
    alias(libs.plugins.stampit.android.library)
    alias(libs.plugins.stampit.android.compose)
}

android {
    namespace = "it.stamp.ui"

    kotlin {
        compilerOptions {
            optIn.addAll("androidx.compose.material3.ExperimentalMaterial3Api")
        }
    }
}

dependencies {
    api(projects.core.designsystem)
}