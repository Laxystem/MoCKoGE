package quest.laxla.mockoge.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import java.io.File

abstract class BundleTask : DefaultTask() {
    private val config = project.extensions.getByType<BundlerExtension>()

    @TaskAction
    fun bundle() {
        createMissingPropertyFile()
        extractVersionFromPropertyFile()

        project.dependencies {
            val dependency = "quest.laxla.mockoge:gradle:${config.mockoge}"

            project.configurations.matching { it.name.startsWith("ksp") && it.name != "ksp" }.forEach {
                add(it.name, dependency)
            }
        }
    }

    private fun createMissingPropertyFile() {
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
    }

    private fun extractVersionFromPropertyFile() {
        if (config.isExtractingVersionFromPropertyFile) project.version = project.properties[config.versionPropertyName]
            ?: throw NullPointerException("Missing property '${config.versionPropertyName}' in gradle.properties")
    }

    companion object {
        const val NAME = "bundle"
    }
}
