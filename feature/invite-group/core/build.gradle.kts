plugins {
    alias(libs.plugins.stampit.android.feature.core)
}

android.namespace = "it.stamp.invite.group.core"

dependencies {
    implementation(projects.feature.inviteGroup.api)
}