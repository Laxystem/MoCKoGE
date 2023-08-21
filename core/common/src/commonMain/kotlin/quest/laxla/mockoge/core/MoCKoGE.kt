package quest.laxla.mockoge.core

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

public data object MoCKoGE : NamespaceProvider("mockoge") {
    internal val logger: KLogger = KotlinLogging.logger(namespace)

    public suspend fun init() {
        RootRegistry.isRegistryFrozen = false
    }
}
