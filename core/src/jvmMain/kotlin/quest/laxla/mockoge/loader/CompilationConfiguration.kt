package quest.laxla.mockoge.loader

import quest.laxla.mockoge.Bundle
import quest.laxla.mockoge.util.VersionDSL
import quest.laxla.mockoge.util.jar
import quest.laxla.mockoge.util.resource
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.jvm

public object CompilationConfiguration : ScriptCompilationConfiguration(
    body = {
        defaultImports(BundleScript::class, BundleScript.Companion::class, Bundle.Relation::class)
        defaultImports(
            "${Bundle.Relation::class.qualifiedName}.*",
            "io.github.z4kn4fein.semver.*",
            "io.github.z4kn4fein.semver.constraints.*",
            "${VersionDSL::class.qualifiedName!!.substringBeforeLast(".")}.*",
            "${VersionDSL::class.qualifiedName!!}.*"
        )

        jvm {
            BundleKts::class.resource?.jar?.name?.let {
                dependenciesFromClassContext(
                    BundleKts::class,
                    KotlinVersion::class.resource!!.jar!!.name
                )
            } ?: dependenciesFromClassContext(BundleKts::class, wholeClasspath = true)
        }

        ide {
            acceptedLocations(ScriptAcceptedLocation.Everywhere)
        }
    }
) {
    private fun readResolve(): Any = CompilationConfiguration
}
