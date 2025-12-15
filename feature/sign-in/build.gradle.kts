import java.util.Properties

plugins {
    alias(libs.plugins.stampit.android.feature)
}

android.namespace = "it.stamp.signin"

val localProperties = Properties().apply {
    rootProject.file("local.properties").inputStream().use {
        load(it)
    }
}

android {
    defaultConfig {
        buildConfigField("String", "GOOGLE_SERVER_CLIENT_ID", "\"${localProperties.getProperty("GOOGLE_SERVER_CLIENT_ID")}\"")
    }
    buildFeatures.buildConfig = true
}

dependencies {
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
}