plugins {
    alias(libs.plugins.stampit.android.library)
    alias(libs.plugins.stampit.android.compose)
}

android {
    namespace = "it.stamp.designsystem"
}

dependencies {
    api(libs.compose.material3)
}