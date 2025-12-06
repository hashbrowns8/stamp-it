plugins {
    alias(libs.plugins.stampit.jvm.library)
}

dependencies {
    api(projects.core.model)

    implementation(libs.javax.inject)
}