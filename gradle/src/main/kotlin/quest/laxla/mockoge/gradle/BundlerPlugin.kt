package quest.laxla.mockoge.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

private const val PluginName = "bundler"
private const val Entrypoint = "quest.laxla.mockoge.main"

class BundlerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.apply(plugin = "org.jetbrains.kotlin.multiplatform")
        project.apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
        project.apply(plugin = "com.google.devtools.ksp")

        project.extensions.create<BundlerExtension>(PluginName)
        project.tasks.register<BundleTask>(BundleTask.NAME) {
            description = "Configures Kotlin/Multiplatform and enables codegen required for MoCKoGE."
            group = "Build"
        }
    }
}
