package quest.laxla.mockoge.core

import quest.laxla.mockoge.core.util.PathPattern
import quest.laxla.mockoge.core.util.at

/**
 * Represents an object that is capable of generating [Identifier]s of the given [namespace].
 *
 * @property namespace the [Identifier.namespace] of the [Identifier] this provider can create.
 * @see [at]
 */
public sealed class NamespaceProvider(public val namespace: String) {
    init {
        if (!namespace.matches(PathPattern)) throw IllegalArgumentException("Namespace $namespace must match regex [$PathPattern]")
    }
}
