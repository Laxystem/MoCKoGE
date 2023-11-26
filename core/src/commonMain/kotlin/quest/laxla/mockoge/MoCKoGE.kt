package quest.laxla.mockoge

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import okio.Path.Companion.toPath
import quest.laxla.mockoge.util.*
import quest.laxla.mockoge.util.FileSystem
import quest.laxla.mockoge.util.SecondaryFileSystems

public data object MoCKoGE : NamespaceProvider(namespace = "mockoge") {
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

        RootRegistry.isMutable = true

        @Suppress(UnusedVariable) // TODO
        val bundles = SecondaryFileSystems.allMatchesFor {
            list(CurrentDirectory).asSequence().filter {
                it.name.endsWith(BundleFileExtension) && metadataOrNull(it)?.isDirectory == false
            }
        }

        // todo: load bundles, lol.
    }
}
