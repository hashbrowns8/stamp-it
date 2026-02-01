plugins {
    alias(libs.plugins.stampit.android.feature.core)
}

android.namespace = "it.stamp.membership.core"

dependencies {
    implementation(projects.feature.membership.api)
}