plugins {
    alias(libs.plugins.stampit.android.feature.core)
}

android.namespace = "it.stamp.edit.profile.core"

dependencies {
    implementation(projects.feature.editProfile.api)
}