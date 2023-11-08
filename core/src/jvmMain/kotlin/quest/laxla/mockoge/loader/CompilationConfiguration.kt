package quest.laxla.mockoge.loader

import quest.laxla.mockoge.Bundle
import quest.laxla.mockoge.util.VersionDSL
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
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
            dependenciesFromCurrentContext() // TODO: restrict
        }

        ide {
            acceptedLocations(ScriptAcceptedLocation.Everywhere)
        }


    }
) {
    private fun readResolve(): Any = CompilationConfiguration
}
