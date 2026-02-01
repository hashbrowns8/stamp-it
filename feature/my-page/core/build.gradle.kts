plugins {
    alias(libs.plugins.stampit.android.feature.core)
}

android.namespace = "it.stamp.mypage.core"

dependencies {
    implementation(projects.feature.myPage.api)
    implementation(projects.feature.editProfile.api)
    implementation(projects.feature.membership.api)
    implementation(projects.feature.inviteMember.api)
    implementation(projects.feature.joinGroup.api)
}