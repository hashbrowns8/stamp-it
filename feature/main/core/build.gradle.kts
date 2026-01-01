plugins {
    alias(libs.plugins.stampit.android.feature.core)
}

android.namespace = "it.stamp.main.core"

dependencies {
    implementation(projects.feature.main.api)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.core)
    implementation(projects.feature.missions.api)
    implementation(projects.feature.missions.core)
    implementation(projects.feature.myPage.api)
    implementation(projects.feature.myPage.core)

    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.navigation3.ui)
}