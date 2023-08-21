package quest.laxla.mockoge.gradle

import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.utils.property
import org.gradle.kotlin.dsl.*

abstract class BundlerExtension {
    private val _isInternal = property { false }
    var isInternal by _isInternal

    private val _isConsideringAllWarningsAsErrors = property { isInternal }
    var isConsideringAllWarningsAsErrors by _isConsideringAllWarningsAsErrors

    private val _isDisablingKotlinDeprecationAndBugfixGracePeriod = property { isInternal }
    var isDisablingKotlinDeprecationAndBugfixGracePeriod by _isDisablingKotlinDeprecationAndBugfixGracePeriod

    private val _isAutomaticallyCreatingMissingPropertyFile = property { true }
    var isAutomaticallyCreatingMissingPropertyFile by _isAutomaticallyCreatingMissingPropertyFile

    private val _isExtractingVersionFromPropertyFile = property { true }
    var isExtractingVersionFromPropertyFile by _isExtractingVersionFromPropertyFile

    private val _versionPropertyName = property { "version" }
    var versionPropertyName by _versionPropertyName

    val NamedDomainObjectContainer<KotlinSourceSet>.common get() = named<KotlinSourceSet>("commonMain")
    val NamedDomainObjectContainer<KotlinSourceSet>.test get() = named<KotlinSourceSet>("commonTest")
    val NamedDomainObjectContainer<KotlinSourceSet>.java get() = named<KotlinSourceSet>("javaMain")
    // javaTest doesn't exist
    val NamedDomainObjectContainer<KotlinSourceSet>.jvm get() = named<KotlinSourceSet>("jvmMain")
    val NamedDomainObjectContainer<KotlinSourceSet>.jvmTest get() = named<KotlinSourceSet>("jvmTest")
    // androidMain
    // androidTest
    val NamedDomainObjectContainer<KotlinSourceSet>.native get() = named<KotlinSourceSet>("nativeMain")
    // nativeTest
    val NamedDomainObjectContainer<KotlinSourceSet>.linux get() = named<KotlinSourceSet>("linuxMain")
    // linuxTest
    val NamedDomainObjectContainer<KotlinSourceSet>.darwin get() = named<KotlinSourceSet>("darwinMain")
    // darwinTest
    // wasmMain
    // wasmTest
}
