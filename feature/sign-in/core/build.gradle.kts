import java.util.Properties

plugins {
    alias(libs.plugins.stampit.android.feature.core)
}

android.namespace = "it.stamp.signin.core"

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
    implementation(projects.feature.signIn.api)
    implementation(projects.feature.main.api)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.navigation3.ui)
}