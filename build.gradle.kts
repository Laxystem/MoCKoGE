plugins {
    kotlin("multiplatform") apply false
    kotlin("jvm") apply false
    kotlin("plugin.serialization") apply false
    id("com.android.library") apply false
}

allprojects {
    group = "quest.laxla.mockoge"
    version = project.properties["version"]!!

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}