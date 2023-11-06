package quest.laxla.mockoge.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private const val PluginName = "bundler"
private const val Entrypoint = "quest.laxla.mockoge.main"

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
            }

            val kspTaskName = "ksp${targetName.replaceFirstChar(Char::titlecase)}"

            project.configurations.matching { it.name == kspTaskName }.all {
                project.dependencies.add(name, dependency)
            }
        }
    }

    companion object {
        val version = BundlerPlugin::class.java.classLoader.getResource(".mockoge")!!.readText()
    }
}
