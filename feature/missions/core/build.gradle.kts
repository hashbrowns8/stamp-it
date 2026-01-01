plugins {
    alias(libs.plugins.stampit.android.feature.core)
}

android.namespace = "it.stamp.missions.core"

dependencies {
    implementation(projects.feature.missions.api)
}