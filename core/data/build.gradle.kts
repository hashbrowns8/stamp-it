plugins {
    alias(libs.plugins.stampit.android.library)
    alias(libs.plugins.stampit.android.hilt)
}

android.namespace = "it.stamp.data"

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.coroutines.play.services)
}