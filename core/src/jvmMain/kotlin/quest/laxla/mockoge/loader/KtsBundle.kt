@file:Suppress(BetaMultiplatformClassesAndObjects)

package quest.laxla.mockoge.loader

import quest.laxla.mockoge.Bundle
import quest.laxla.mockoge.util.BetaMultiplatformClassesAndObjects
import quest.laxla.mockoge.util.BundleFileExtension
import quest.laxla.mockoge.util.VersionDSL
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

@KotlinScript(
    displayName = "MoCKoGE Bundle",
    fileExtension = BundleFileExtension,
    compilationConfiguration = CompilationConfiguration::class
)
public abstract class KtsBundle : BundleScript()

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
