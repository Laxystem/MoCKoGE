package quest.laxla.mockoge

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import okio.Path.Companion.toPath
import quest.laxla.mockoge.loader.BundleScript
import quest.laxla.mockoge.loader.Bundleable
import quest.laxla.mockoge.util.BasePath
import quest.laxla.mockoge.util.FileSystem

public data object MoCKoGE : NamespaceProvider("mockoge") {
    internal val logger: KLogger = KotlinLogging.logger(namespace)

    public val isDevelopmentEnvironment: Boolean get() = FileSystem.exists("build.gradle.kts".toPath())

    public suspend fun init() {
        logger.info { "Initializing MoCKoGE..." }
        logger.info { "Kotlin: ${KotlinVersion.CURRENT}" }
        logger.info { "Development Environment: $isDevelopmentEnvironment" }
        logger.info { "Base Path: $BasePath" }

        if (!FileSystem.exists(BasePath)) {
            logger.info { "Creating base path..." }
            FileSystem.createDirectories(BasePath, mustCreate = false)
            FileSystem.createDirectory(BasePath)
        }

        RootRegistry.isRegistryFrozen = false

    // todo: load bundles, lol.
    }
}

@Bundleable
public data object TemporaryBundle : BundleScript()