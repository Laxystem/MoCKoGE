import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

val jvm: String by project
val klogging: String by project
val coroutines: String by project
val serialization: String by project

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    explicitApi()

    targets.all {
        compilations.all {
            compilerOptions.configure {
                allWarningsAsErrors = true
            }
        }
    }

    val linux = listOf(
        linuxArm64(),
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
        watchosArm32(),
        watchosX64(),
        tvosArm64(),
        tvosSimulatorArm64(),
        tvosX64()
    )

    jvm {
        jvmToolchain(jvm.toInt())
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    wasm {
        d8 {

        }

        browser {

        }
    }

    androidTarget()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("io.github.oshai:kotlin-logging:$klogging")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization")
            }
        }

        val commonTest by getting {
            dependencies {
                api(kotlin("test"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines")
            }
        }

        val javaMain by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(javaMain)
            // TODO: SLF4J java.util.logging
        }

        val androidMain by getting {
            dependsOn(javaMain)
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val linuxMain by creating {
            dependsOn(nativeMain)
        }

        val darwinMain by creating {
            dependsOn(nativeMain)
        }

        val wasmMain by getting

        linux.forEach {

        }

        darwin.forEach {

        }
    }
}


android {
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")


    namespace = group.toString()
}