plugins {
    alias(libs.plugins.stampit.android.library)
    alias(libs.plugins.stampit.android.hilt)
}

android.namespace = "it.stamp.navigation"

dependencies {
    api(libs.androidx.navigation3.runtime)
}