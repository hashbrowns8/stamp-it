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
include(":core:database")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:navigation")
include(":core:ui")
include(":feature:home:api")
include(":feature:home:core")
include(":feature:invite-group:api")
include(":feature:invite-group:core")
include(":feature:main:api")
include(":feature:main:core")
include(":feature:missions:api")
include(":feature:missions:core")
include(":feature:my-page:api")
include(":feature:my-page:core")
include(":feature:sign-in:api")
include(":feature:sign-in:core")
include(":feature:join-group:api")
include(":feature:join-group:core")
include(":core:common")
