package quest.laxla.mockoge.gradle

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

private const val PluginName = "bundler"
private const val Entrypoint = "quest.laxla.mockoge.main"

class BundlerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.apply(plugin = "org.jetbrains.kotlin.multiplatform")
        project.apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
        project.apply(plugin = "com.google.devtools.ksp")

        val config = project.extensions.create<BundlerExtension>(PluginName)
        val bundler = project.tasks.register<BundleTask>(BundleTask.NAME) {
            description = "Configures Kotlin/Multiplatform and enables codegen required for MoCKoGE."
            group = "Build"
        }

        val jvm: String by project

        config.advanced.run {
            explicitApi = config.explicitApi

            jvm {
                jvmToolchain(jvm.toInt())
                testRuns["test"].executionTask.configure {
                    useJUnitPlatform()
                }
            }

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

                val commonTest by getting {
                    dependencies {
                        api(kotlin("test"))
                    }
                }

                val javaMain by creating { dependsOn(commonMain) }

                val jvmMain by getting { dependsOn(javaMain) }

                val nativeMain by creating { dependsOn(commonMain) }

                val linuxMain by creating { dependsOn(nativeMain) }

                val darwinMain by creating { dependsOn(nativeMain) }

                configure(linux, linuxMain)
                configure(darwin, darwinMain)
            }

            targets.all {
                compilations.all {
                    project.tasks.getByName(compileAllTaskName).dependsOn(bundler)
                    project.tasks.getByName(compileKotlinTaskName).dependsOn(bundler)
                }
            }
        }

        project.dependencies {
            val dependency = "quest.laxla.mockoge:gradle:${config.mockoge}"

            project.configurations.matching { it.name.startsWith("ksp") && it.name != "ksp" }.forEach {
                add(it.name, dependency)
            }
        }
    }

    private fun NamedDomainObjectContainer<KotlinSourceSet>.configure(
        nativeTargets: List<KotlinNativeTarget>,
        mainSourceSet: KotlinSourceSet
    ) = nativeTargets.forEach {
        it.binaries.executable {
            entryPoint = Entrypoint
        }

        getByName(it.targetName + "Main") {
            dependsOn(mainSourceSet)
        }

        getByName(it.targetName + "Test") { }
    }
}
