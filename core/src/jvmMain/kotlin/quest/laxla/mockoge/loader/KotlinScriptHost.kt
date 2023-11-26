package quest.laxla.mockoge.loader

import quest.laxla.mockoge.MoCKoGE
import quest.laxla.mockoge.util.toLogLevel
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmScriptDefinitionFromTemplate

private val defaultFailureMessage = { "KotlinScript compilation failed" }

internal class KotlinScriptHost {
    private val host = BasicJvmScriptingHost()
    private val template = createJvmScriptDefinitionFromTemplate<BundleKts>()

    operator fun invoke(
        source: SourceCode,
        failureMessage: () -> String = defaultFailureMessage
    ): EvaluationResult? {
        val result = host.eval(
            source,
            template.compilationConfiguration,
            template.evaluationConfiguration
        )

        result.reports.forEach {
            MoCKoGE.logger.at(it.severity.toLogLevel()) {
                message = "KotlinScript ${it.severity}${
                    it.location?.start?.let { pos ->
                        " at ${pos.line}:${pos.col}"
                    } ?: ""
                } -- ${it.message}"
                cause = it.exception
            }
        }

        return when (result) {
            is ResultWithDiagnostics.Failure -> {
                MoCKoGE.logger.warn(failureMessage)
                null
            }

            is ResultWithDiagnostics.Success -> result.value
        }
    }
}