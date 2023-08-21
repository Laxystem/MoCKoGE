package quest.laxla.mockoge.core.loader

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import okio.BufferedSource
import quest.laxla.mockoge.core.MoCKoGE

public object JsonBundleLoader : BundleLoader() {
    override val fileExtension: String get() = ".json"

    @OptIn(ExperimentalSerializationApi::class)
    override fun load(file: BufferedSource, namespace: String): BundleData? = try {
        Json.decodeFromBufferedSource(file)
    } catch (e : IllegalArgumentException) {
        MoCKoGE.logger.warn(e) { "Failed to decode bundle-file of [$namespace]. Skipping" }
        null
    }
}
