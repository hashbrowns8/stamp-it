plugins {
    alias(libs.plugins.stampit.jvm.library)
    alias(libs.plugins.ksp)
}

dependencies {
    api(projects.core.model)
    api(libs.coroutines.core)
    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)
}