import java.io.FileInputStream
import java.util.*

plugins {
    `kotlin-dsl`
}

group = "quest.laxla.mockoge"

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}

val properties = Properties().apply {
    load(FileInputStream(File(rootDir.parent, "gradle.properties")))
}

version = properties["version"]!!

val kotlin: String by properties
val ksp: String by properties
val poet: String by properties

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kotlin-$ksp")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:$kotlin-$ksp")
    implementation("com.squareup:kotlinpoet:$poet")
    implementation("com.squareup:kotlinpoet-ksp:$poet")
}

gradlePlugin {
    plugins {
        create("bundler") {
            id = "quest.laxla.mockoge.bundler"
            implementationClass = "quest.laxla.mockoge.gradle.BundlerPlugin"
            displayName = "MoCKoGE Bundler"
            description = "The official MoCKoGE plugin, allows fatjaring MoCKoGE & Native Compilation"
        }
    }
}

val outputDir: Directory = layout.buildDirectory.dir("generated/resources").get()

tasks {
    val bundle = create("bundle") {
        val outputFile = outputDir.file(".mockoge")
        outputs.file(outputFile)

        doLast {
            outputFile.asFile.writeText(version.toString())
        }
    }

    processResources {
        dependsOn(bundle)
    }
}

sourceSets.main {
    resources.srcDir(outputDir)
}
