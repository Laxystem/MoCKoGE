@file:Suppress(BetaMultiplatformClassesAndObjects)

package quest.laxla.mockoge.loader.internal

import quest.laxla.mockoge.Bundle
import quest.laxla.mockoge.loader.BundleScript
import quest.laxla.mockoge.util.BetaMultiplatformClassesAndObjects
import quest.laxla.mockoge.util.VersionDSL
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.ScriptingHostConfiguration
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

public actual typealias KotlinScript = KotlinScript

public actual typealias KtsCompileConfig = ScriptCompilationConfiguration
public actual object KtsCompileConfigDefault : ScriptCompilationConfiguration(
    body = {
        defaultImports(VersionDSL::class, BundleScript::class, BundleScript.Companion::class, Bundle.Relation::class)
        defaultImports("${Bundle.Relation::class.qualifiedName}.*")

        jvm {
            dependenciesFromCurrentContext() // TODO: restrict
        }

        ide {
            acceptedLocations(ScriptAcceptedLocation.Everywhere)
        }
    }
) {
    private fun readResolve(): Any = KtsCompileConfigDefault
}

public actual typealias KtsEvalConfig = ScriptEvaluationConfiguration
public actual typealias KtsEvalConfigDefault = ScriptEvaluationConfiguration.Default

public actual typealias KtsHostConfig = ScriptingHostConfiguration
