package quest.laxla.mockoge.core

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import okio.Path.Companion.toPath
import okio.buffer
import quest.laxla.mockoge.core.loader.BundleData
import quest.laxla.mockoge.core.loader.BundleLoader
import quest.laxla.mockoge.core.loader.BundleLoaderRegistry
import quest.laxla.mockoge.core.util.*

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

        load(BundleLoaderRegistry.default)
    }


    private suspend fun load(loader: BundleLoader): Sequence<BundleData> = SecondaryFileSystems.allMatchesFor {
        listRecursively(CurrentDirectory).filter { it.name.endsWith(loader.fileExtension) }.mapNotNull {
            loader.load(source(it).buffer()) otherwise {
                logger.warn { "Failed to load bundle from [${it.name}]. Skipping" }
            }
        }
    }
}
