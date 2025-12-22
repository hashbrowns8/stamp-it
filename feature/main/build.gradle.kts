plugins {
    alias(libs.plugins.stampit.android.feature)
}

android.namespace = "it.stamp.main"

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.missions)
    implementation(projects.feature.myPage)
    implementation(libs.androidx.navigation3.ui)
}