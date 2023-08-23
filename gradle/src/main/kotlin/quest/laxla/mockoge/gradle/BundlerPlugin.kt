package quest.laxla.mockoge.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import java.io.File

private const val Entrypoint = "quest.laxla.mockoge.core.main"

class BundlerPlugin : Plugin<Project> {

    lateinit var linux: List<KotlinNativeTargetWithHostTests> private set
    lateinit var darwin: List<KotlinNativeTarget> private set

    override fun apply(project: Project) {
        project.apply(plugin = "org.jetbrains.kotlin.multiplatform")

        project.apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
        project.apply(plugin = "com.google.devtools.ksp")

        val config = project.extensions.create<BundlerExtension>("bundler")

        if (config.isAutomaticallyCreatingMissingPropertyFile) File(project.rootDir, "gradle.properties").apply {
            parentFile.mkdirs()
            if (createNewFile()) outputStream().writer().use {

                if (config.isExtractingVersionFromPropertyFile) {
                    //language=properties
                    writeText(
                        """
${config.versionPropertyName}=0.0.1-alpha

                    """.trimIndent()
                    )
                }

                //language=properties
                writeText(
                    """
# Dependencies
jvm=17

# Config
kotlin.native.ignoreDisabledTargets=true
kotlin.code.style=official

                """.trimIndent()
                )
            }
        }

        if (config.isExtractingVersionFromPropertyFile) project.version = project.properties[config.versionPropertyName]
            ?: throw NullPointerException("Missing property '${config.versionPropertyName}' in gradle.properties")

        val jvm: String by project

        config.advanced.run {
            explicitApi = config.explicitApi

            jvm {
                jvmToolchain(jvm.toInt())
                testRuns["test"].executionTask.configure {
                    useJUnitPlatform()
                }
            }

            linux = listOf(
                //linuxArm64(), //todo: https://github.com/Kotlin/kotlinx-datetime/issues/300 https://github.com/square/okio/issues/1242 https://github.com/Kotlin/kotlinx.collections.immutable/issues/145
                linuxX64(),
                mingwX64()
            )

            darwin = listOf(
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

                val commonTest by getting {
                    dependencies {
                        api(kotlin("test"))
                    }
                }

                val javaMain by creating {
                    dependsOn(commonMain)
                }

                val jvmMain by getting {
                    dependsOn(javaMain)

                    dependencies {
                        if (config.isReflectionEnabled) implementation(kotlin("reflect"))
                    }
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
                    it.binaries.executable {
                        entryPoint = Entrypoint
                    }

                    getByName(it.targetName + "Main") {
                        dependsOn(linuxMain)
                    }
                }

                darwin.forEach {
                    it.binaries.executable {
                        entryPoint = Entrypoint
                    }

                    getByName(it.targetName + "Main") {
                        dependsOn(darwinMain)
                    }
                }
            }
        }

        project.dependencies {
            add("kspJvm", "quest.laxla.mockoge:gradle:${config.mockoge}")
        }
    }
}
