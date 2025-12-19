package it.stamp

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

internal inline fun <reified T: KotlinBaseExtension> Project.configureKotlin() = configure<T> {
    kotlinExtension.jvmToolchain(17)

    when (this) {
        is KotlinJvmProjectExtension -> compilerOptions
        is KotlinAndroidProjectExtension -> compilerOptions
        else -> throw Exception()
    }.apply {
        optIn.addAll(
            "kotlin.time.ExperimentalTime",
            "kotlin.uuid.ExperimentalUuidApi",
            "kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }
}

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) = with(commonExtension) {
    configureKotlin<KotlinAndroidProjectExtension>()

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 29
    }
}