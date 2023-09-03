plugins {
    id("org.jetbrains.dokka")
}

allprojects {
    apply(plugin = "org.jetbrains.dokka")

    group = "quest.laxla.mockoge"

    repositories {
        mavenCentral()
    }
}
