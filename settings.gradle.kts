pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.9.2"
        id("org.jetbrains.kotlin.android") version "1.8.20"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Supabase может быть в JitPack
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "DriveNext"
include(":app")

