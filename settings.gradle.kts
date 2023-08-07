pluginManagement {
    repositories {
        google()
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

rootProject.name = "MusicApp"
include(":app")
include(":feature:songs")
include(":core:data")
include(":core:model")
include(":player")
include(":core:common")
include(":feature:playercontrol")
include(":feature:search")
