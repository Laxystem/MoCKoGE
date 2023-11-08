package quest.laxla.mockoge.util

import io.github.oshai.kotlinlogging.Level
import kotlin.script.experimental.api.ScriptDiagnostic

public fun ScriptDiagnostic.Severity.toLogLevel(): Level = when (this) {
    ScriptDiagnostic.Severity.DEBUG -> Level.DEBUG
    ScriptDiagnostic.Severity.INFO -> Level.INFO
    else -> Level.WARN
}