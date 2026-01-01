plugins {
    alias(libs.plugins.stampit.android.feature.core)
}

android.namespace = "it.stamp.home.core"

dependencies {
    implementation(projects.feature.home.api)
    implementation(projects.feature.inviteGroup.api)
}