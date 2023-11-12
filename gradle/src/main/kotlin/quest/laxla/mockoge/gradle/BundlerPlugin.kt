package quest.laxla.mockoge.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.language.jvm.tasks.ProcessResources
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

private const val PluginName = "bundler"
private const val Entrypoint = "quest.laxla.mockoge.MainKt"
private const val StandaloneJarName = "standalone"

class BundlerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.apply(plugin = "org.jetbrains.kotlin.multiplatform")
        project.apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
        project.apply(plugin = "com.google.devtools.ksp")

        project.extensions.create<BundlerExtension>(PluginName)
        val bundler = project.tasks.register<BundleTask>(BundleTask.NAME) {
            description = "Configures Kotlin/Multiplatform and enables codegen required for MoCKoGE."
            group = "Build"
        }

        val dependency = "quest.laxla.mockoge:gradle:$version"

        project.extensions.getByType<KotlinMultiplatformExtension>().targets.all {
            compilations.all {
                project.tasks.getByName(compileAllTaskName).dependsOn(bundler)
                project.tasks.getByName(compileKotlinTaskName).dependsOn(bundler)

                if (platformType == KotlinPlatformType.jvm) {
                    val standaloneJar by project.tasks.register<Jar>(name = StandaloneJarName + artifactsTaskName.chain() + compilationName.chain()) {
                        archiveClassifier.set(StandaloneJarName)
                        manifest.attributes += "Main-Class" to Entrypoint

                        val files = runtimeDependencyFiles!!.elements.map { runtimeDependencies ->
                            project.files(runtimeDependencies.map { jar ->
                                project.zipTree(jar)
                            })
                        }

                        inputs.files(files)
                        from(files)

                        duplicatesStrategy = DuplicatesStrategy.WARN
                    }

                    project.tasks.matching { it.name == artifactsTaskName }.all {
                        val files = inputs.files

                            standaloneJar.inputs.files(files)
                        standaloneJar.from(files)
                    }
                }
            }

            val kspTaskName = "ksp${targetName.replaceFirstChar(Char::titlecase)}"

            project.configurations.matching { it.name == kspTaskName }.all {
                project.dependencies.add(name, dependency)
            }
        }

        @Suppress("UnstableApiUsage")
        project.tasks.withType<ProcessResources>().configureEach task@{
            doFirst {
                filesMatching("**/*.bundle.kts") {
                    expand("projectVersion" to this@task.project.version, "mockogeVersion" to version)
                }
            }
        }
    }

    companion object {
        val version = BundlerPlugin::class.java.classLoader.getResource(".mockoge")!!.readText()
    }
}

private fun String.chain() = replaceFirstChar(Char::titlecase)
