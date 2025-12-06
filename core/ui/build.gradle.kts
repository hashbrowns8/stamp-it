plugins {
    alias(libs.plugins.stampit.android.library)
    alias(libs.plugins.stampit.android.compose)
}

android {
    namespace = "it.stamp.ui"
}

dependencies {
    api(projects.core.designsystem)
}