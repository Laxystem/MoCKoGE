import java.io.FileInputStream
import java.util.*

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}

val properties = Properties().apply {
    load(FileInputStream(File(rootDir.parent, "gradle.properties")))
}

val kotlin: String by properties

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
}