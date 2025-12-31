plugins {
    alias(libs.plugins.stampit.android.application)
    alias(libs.plugins.stampit.android.compose)
    alias(libs.plugins.stampit.android.hilt)
    alias(libs.plugins.stampit.firebase)
}

android {
    namespace = "it.stamp"

    defaultConfig {
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.ui)

    implementation(projects.feature.home)
    implementation(projects.feature.inviteGroup)
    implementation(projects.feature.main)
    implementation(projects.feature.memberManagement)
    implementation(projects.feature.missionAssignment)
    implementation(projects.feature.missions)
    implementation(projects.feature.myPage)
    implementation(projects.feature.notifications)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.profile)
    implementation(projects.feature.signIn)
    implementation(projects.feature.splash)
    implementation(projects.feature.stampBoard)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.firebase.analytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}