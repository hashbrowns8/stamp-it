pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "StampIt"
include(":app")
include(":core:data")
include(":core:firestore")
include(":core:database")
include(":core:ui")
include(":core:domain")
include(":feature:home")
include(":feature:main")
include(":feature:stamp-board")
include(":feature:profile")
include(":feature:missions")
include(":core:designsystem")
include(":core:model")
include(":feature:my-page")
include(":feature:onboarding")
include(":feature:login")
include(":feature:notifications")
include(":feature:mission-assignment")
include(":feature:member-management")
