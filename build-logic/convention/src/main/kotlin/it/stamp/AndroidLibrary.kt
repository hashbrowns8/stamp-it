package it.stamp

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

internal fun Project.configureAndroidLibrary(
    libraryExtension: LibraryExtension
) = with(libraryExtension) {
    configureKotlinAndroid(libraryExtension)
}