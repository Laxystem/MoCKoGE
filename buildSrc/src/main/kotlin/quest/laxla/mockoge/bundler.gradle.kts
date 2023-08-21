package quest.laxla.mockoge
// do not add a file-wide annotation; the package must be on the first line.

import org.gradle.kotlin.dsl.*
import quest.laxla.mockoge.gradle.BundlerExtension
import java.io.FileNotFoundException


plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    java
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}

val jvm: String by project
val mockoge: String by project

val config = project.extensions.create<BundlerExtension>("bundler")

val mockogeVersion get() = if (config.isInternal) version else mockoge

try {
    jvm
} catch (e: Exception) {
    val properties = File(rootDir, "gradle.properties")
    if (config.isAutomaticallyCreatingMissingPropertyFile && !properties.exists()) {
        properties.createNewFile()
        //language=Properties
        properties.writeText(
            """
version=0.0.1-alpha
# Dependencies
jvm=17
mockoge= # TODO: put mockoge version here
        """.trimIndent()
        )
        throw FileNotFoundException("File gradle.properties didn't exist, one was created for you. Try to rebuild.")
    } else throw NullPointerException("Missing property 'jvm' in gradle.properties")
}

if (config.isExtractingVersionFromPropertyFile)
    version = project.properties[config.versionPropertyName]
        ?: throw NullPointerException("Missing property '${config.versionPropertyName}' in gradle.properties")

kotlin {
    explicitApi()

    targets.all {
        compilations.all {
            compilerOptions.configure {
                allWarningsAsErrors = config.isConsideringAllWarningsAsErrors
                progressiveMode = config.isDisablingKotlinDeprecationAndBugfixGracePeriod
            }
        }
    }


    jvm {
        jvmToolchain(jvm.toInt())
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    /*
    wasm {
        d8 {

        }

        browser {

        }
    }*/


    //androidTarget()
    // TODO: android target

    val linux = listOf(
        //linuxArm64(), //todo: https://github.com/Kotlin/kotlinx-datetime/issues/300 https://github.com/square/okio/issues/1242 https://github.com/Kotlin/kotlinx.collections.immutable/issues/145
        linuxX64(),
        mingwX64()
    )

    val darwin = listOf(
        macosArm64(),
        macosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        iosX64(),
        watchosArm64(),
        watchosSimulatorArm64(),
        watchosX64(),
        tvosArm64(),
        tvosSimulatorArm64(),
        tvosX64()
    )

    sourceSets {
        val commonMain by getting

        commonTest {
            dependencies {
                api(kotlin("test"))
            }
        }

        val javaMain by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(javaMain)
        }

        /*
        val androidMain by getting {
            dependsOn(javaMain)
        }
         */

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val linuxMain by creating {
            dependsOn(nativeMain)
        }

        val darwinMain by creating {
            dependsOn(nativeMain)
        }

        linux.forEach {
            it.binaries.executable()

            getByName(it.targetName + "Main") {
                dependsOn(linuxMain)
            }
        }

        darwin.forEach {
            it.binaries.executable()

            getByName(it.targetName + "Main") {
                dependsOn(darwinMain)
            }
        }
    }
}
