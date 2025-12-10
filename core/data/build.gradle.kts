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
    implementation(libs.coroutines.play.services)
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
}