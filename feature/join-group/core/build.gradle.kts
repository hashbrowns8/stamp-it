plugins {
    alias(libs.plugins.stampit.android.feature.core)
}

android.namespace = "it.stamp.join.group.core"

dependencies {
    implementation(projects.feature.joinGroup.api)
    implementation(projects.feature.main.api)
}