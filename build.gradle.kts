plugins {
    kotlin("multiplatform") apply false
    kotlin("jvm") apply false
    kotlin("plugin.serialization") apply false
    id("com.android.library") apply false
}

allprojects {
    group = "quest.laxla.mockoge"

    // TODO: remove this
    repositories {
        maven("Sonatype Snapshots") {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}

repositories { mavenCentral() }
