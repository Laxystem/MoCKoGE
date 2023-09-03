rootProject.name = "mockoge"
include("core:common", "core:server", "core:client")

// For dependencies
includeBuild("gradle")

pluginManagement {
    // For plugins
    includeBuild("gradle")

    val dokka: String by settings

    plugins {
        id("org.jetbrains.dokka") version dokka
    }

    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}
