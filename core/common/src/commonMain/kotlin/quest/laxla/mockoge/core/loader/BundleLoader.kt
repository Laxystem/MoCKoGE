package quest.laxla.mockoge.core.loader

import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.constraints.Constraint
import kotlinx.serialization.Serializable
import okio.BufferedSource
import quest.laxla.mockoge.core.Bundle

public abstract class BundleLoader {
    public abstract val fileExtension: String
    public val fullFileExtension: String = ".bundle.$fileExtension" // TODO: turn this to extension or else it'll explode
    public abstract fun load(file: BufferedSource, namespace: String): BundleData?

    @Serializable
    public data class BundleData(val version: Version, val dependencies: List<DependencyData>)

    @Serializable
    public data class DependencyData(val relation: Bundle.BundleDependencyRelation, val constraints: List<Constraint>)

    /**
     * Turns `"example.mockoge.bundle.json"` to `"mockoge/example"`
     */
    public fun namespaceOf(fileName: String): String? = fileName
        .substringBeforeLast(fullFileExtension)
        .takeUnless { it == fileName }
        ?.splitToSequence('.')
        ?.withIndex()
        ?.sortedByDescending { it.index }
        ?.joinToString(separator = "/") { it.value }
/*

    internal fun load(): Sequence<BundleData> {
        fileSystems.allMatchesFor {

        }
    }
*/

    public companion object {
        public val fileExtensionRegex: Regex = "(\\.[a-z]+)+".toRegex()
    }
}
