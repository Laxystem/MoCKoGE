package quest.laxla.mockoge

import quest.laxla.mockoge.loader.KotlinScriptHost
import java.io.File
import kotlin.script.experimental.host.toScriptSource

public actual suspend fun platformMain() {
    KotlinScriptHost()(File(KotlinScriptHost::class.java.getResource("/jvm.mockoge.bundle.kts")!!.toURI()).toScriptSource())
}
