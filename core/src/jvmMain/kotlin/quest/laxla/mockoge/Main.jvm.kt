package quest.laxla.mockoge

import quest.laxla.mockoge.loader.BundleKts
import quest.laxla.mockoge.loader.KotlinScriptHost
import quest.laxla.mockoge.util.fileOrNull
import quest.laxla.mockoge.util.resource
import java.io.File
import kotlin.script.experimental.host.toScriptSource

public actual suspend fun platformMain() {
    println(BundleKts::class.resource?.fileOrNull)
    KotlinScriptHost()(File(KotlinScriptHost::class.java.getResource("/jvm.mockoge.bundle.kts")!!.toURI()).toScriptSource())
}
