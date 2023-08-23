package quest.laxla.mockoge.core.loader

import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable

@Serializable
public data class BundleData(
    val version: Version,
    val dependencies: Map<IdentifierData, BundleDependencyData>,
    val definitions: Map<IdentifierData, Map<String, KClassData>>
)
