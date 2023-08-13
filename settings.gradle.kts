rootProject.name = "mockoge"
include("core")

pluginManagement {
    val kotlin: String by settings
    val android: String by settings

    plugins {
        kotlin("multiplatform") version kotlin apply false
        kotlin("jvm") version kotlin apply false
        kotlin("plugin.serialization") version kotlin apply false
        id("com.android.library") version android apply false
    }

    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}