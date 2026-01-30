plugins {
    alias(libs.plugins.stampit.android.feature.core)
}

android.namespace = "it.stamp.invite.member.core"

dependencies {
    implementation(projects.feature.inviteMember.api)
}