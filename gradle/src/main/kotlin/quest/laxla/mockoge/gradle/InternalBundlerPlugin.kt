package quest.laxla.mockoge.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

class InternalBundlerPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.apply<BundlerPlugin>()

        target.extensions.getByType<BundlerExtension>().run {
            explicitApi = ExplicitApiMode.Strict

            compiler {
                progressiveMode = true
                allWarningsAsErrors = true
            }

            isReflectionEnabled = true
        }
    }
}