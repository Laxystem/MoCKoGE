package quest.laxla.mockoge.core.loader

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import okio.BufferedSource

public object JsonBundleLoader : BundleLoader(fileType = "json") {

    @OptIn(ExperimentalSerializationApi::class)
    override fun load(file: BufferedSource): BundleData? = try {
        Json.decodeFromBufferedSource(file)
    } catch (e : IllegalArgumentException) {
        null
    }
}
