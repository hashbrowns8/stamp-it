plugins {
    alias(libs.plugins.stampit.android.library)
    alias(libs.plugins.stampit.android.hilt)
    alias(libs.plugins.stampit.firebase)
}

android.namespace = "it.stamp.data"

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.coroutines.play.services)
}