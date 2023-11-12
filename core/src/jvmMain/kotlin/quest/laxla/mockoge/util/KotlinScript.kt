package quest.laxla.mockoge.util

import io.github.oshai.kotlinlogging.Level
import java.io.File
import java.net.JarURLConnection
import java.net.URISyntaxException
import java.net.URL
import kotlin.reflect.KClass
import kotlin.script.experimental.api.ScriptDiagnostic

public fun ScriptDiagnostic.Severity.toLogLevel(): Level = when (this) {
    ScriptDiagnostic.Severity.DEBUG -> Level.DEBUG
    ScriptDiagnostic.Severity.INFO -> Level.INFO
    else -> Level.WARN
}

public val URL.jar: File? get() = (takeIf { protocol == "jar" }?.openConnection() as? JarURLConnection)?.jarFileURL?.fileOrNull

public val URL.fileOrNull: File?
    get() = try {
        File(toURI())
    } catch (_: IllegalArgumentException) {
        null
    } catch (_: URISyntaxException) {
        null
    } ?: if (protocol == "file") File(this.file) else null

public val <T : Any> KClass<out T>.resource: URL?
    get() = java.classLoader.getResource(java.name.replace(oldChar = '.', newChar = '/') + ".class")