package quest.laxla.mockoge.gradle

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.utils.property

private typealias Kotlin = NamedDomainObjectContainer<KotlinSourceSet>
private typealias Dependencies = KotlinDependencyHandler.() -> Unit

@Suppress("FunctionName")
abstract class BundlerExtension (project: Project) {
    var isAutomaticallyCreatingMissingPropertyFile by property { true }
    var isExtractingVersionFromPropertyFile by property { true }
    var versionPropertyName: String by property { "version" }
    var explicitApi by property { ExplicitApiMode.Disabled }

    val mockoge = BundlerPlugin::class.java.classLoader.getResource(".mockoge")!!.readText()
    fun mockoge(module: String) = "quest.laxla.mockoge:$module:$mockoge"

    val advanced = project.extensions.getByType<KotlinMultiplatformExtension>()
    fun advanced(configure: KotlinMultiplatformExtension.() -> Unit) = advanced.configure()

    val dependencies: Kotlin get() = advanced.sourceSets
    fun dependencies(configure: Kotlin.() -> Unit) = dependencies.configure()

    fun compiler(configure: KotlinCommonCompilerOptions.() -> Unit) = advanced.targets.all {
        compilations.all {
            compilerOptions.configure {
                configure()
            }
        }
    }

    fun Kotlin.common(configure: Dependencies) = named<KotlinSourceSet>("commonMain") { dependencies(configure) }
    fun Kotlin.tests(configure: Dependencies) = named<KotlinSourceSet>("commonTest") { dependencies(configure) }
    fun Kotlin.java(configure: Dependencies) = named<KotlinSourceSet>("javaMain") { dependencies(configure) }
    fun Kotlin.`java tests`(configure: Dependencies) = named<KotlinSourceSet>("javaTest") { dependencies(configure) }
    fun Kotlin.jvm(configure: Dependencies) = named<KotlinSourceSet>("jvmMain") { dependencies(configure) }
    fun Kotlin.`jvm tests`(configure: Dependencies) = named<KotlinSourceSet>("jvmTest") { dependencies(configure) }
    fun Kotlin.native(configure: Dependencies) = named<KotlinSourceSet>("nativeMain") { dependencies(configure) }
    fun Kotlin.`native tests`(configure: Dependencies) = named<KotlinSourceSet>("nativeTest") { dependencies(configure) }
    fun Kotlin.linux(configure: Dependencies) = named<KotlinSourceSet>("linuxMain") { dependencies(configure) }
    fun Kotlin.`linux tests`(configure: Dependencies) = named<KotlinSourceSet>("linuxTest") { dependencies(configure) }
    fun Kotlin.darwin(configure: Dependencies) = named<KotlinSourceSet>("darwinMain") { dependencies(configure) }
    fun Kotlin.`darwin tests`(configure: Dependencies) = named<KotlinSourceSet>("darwinTest") { dependencies(configure) }
}
