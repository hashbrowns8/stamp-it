plugins {
    alias(libs.plugins.stampit.android.feature.core)
}

android.namespace = "it.stamp.mypage.core"

dependencies {
    implementation(projects.feature.myPage.api)
    implementation(projects.feature.inviteGroup.api)
    implementation(projects.feature.joinGroup.api)
}