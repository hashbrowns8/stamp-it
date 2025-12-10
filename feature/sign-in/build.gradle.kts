plugins {
    alias(libs.plugins.stampit.android.feature)
}

android.namespace = "it.stamp.signin"

android {
    buildFeatures.buildConfig = true
}

dependencies {
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
}