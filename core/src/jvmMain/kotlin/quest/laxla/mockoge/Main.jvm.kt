package quest.laxla.mockoge

import quest.laxla.mockoge.loader.KotlinScriptHost
import kotlin.script.experimental.host.toScriptSource

public actual suspend fun platformMain() {
    val host = KotlinScriptHost()

    host(KotlinScriptHost::class.java.getResourceAsStream("/jvm.mockoge.bundle.kts")!!.use { it.reader().readText() }.toScriptSource())
}
