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

abstract class BundlerExtension (private val project: Project) {
    var isAutomaticallyCreatingMissingPropertyFile by property { true }
    var isExtractingVersionFromPropertyFile by property { true }
    var versionPropertyName: String by property { "version" }
    var isReflectionEnabled by property { false }
    var explicitApi by property { ExplicitApiMode.Disabled }

    val mockoge = BundlerPlugin::class.java.classLoader.getResource(".mockoge")!!.readText()
    fun mockoge(module: String) = "quest.laxla.mockoge:$module:$mockoge"

    val advanced get() = project.extensions.getByType<KotlinMultiplatformExtension>()
    inline fun advanced(configure: KotlinMultiplatformExtension.() -> Unit) = advanced.configure()

    val dependencies get() = advanced.sourceSets
    inline fun compiler(crossinline configure: KotlinCommonCompilerOptions.() -> Unit) = advanced.targets.all {
        compilations.all {
            compilerOptions.configure {
                configure()
            }
        }
    }

    fun Kotlin.common(configure: Dependencies) = named<KotlinSourceSet>("commonMain") { dependencies(configure) }
    fun Kotlin.test(configure: Dependencies) = named<KotlinSourceSet>("commonTest") { dependencies(configure) }
    fun Kotlin.java(configure: Dependencies) = named<KotlinSourceSet>("javaMain") { dependencies(configure) }
    fun Kotlin.jvm(configure: Dependencies) = named<KotlinSourceSet>("jvmMain") { dependencies(configure) }
    fun Kotlin.jvmTest(configure: Dependencies) = named<KotlinSourceSet>("jvmTest") { dependencies(configure) }
    fun Kotlin.native(configure: Dependencies) = named<KotlinSourceSet>("nativeMain") { dependencies(configure) }
    fun Kotlin.linux(configure: Dependencies) = named<KotlinSourceSet>("linuxMain") { dependencies(configure) }
    fun Kotlin.darwin(configure: Dependencies) = named<KotlinSourceSet>("darwinMain") { dependencies(configure) }
}
