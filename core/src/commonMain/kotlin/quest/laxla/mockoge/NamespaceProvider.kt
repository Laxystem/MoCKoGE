package quest.laxla.mockoge

import quest.laxla.mockoge.Identifier.Companion.NamespacePattern

/**
 * Represents an object that is capable of generating [Identifier]s of the given [namespace].
 *
 * @property namespace the [Identifier.namespace] of the [Identifier] this provider can create.
 * @see [at]
 */
public sealed class NamespaceProvider(public val namespace: String) {
    init {
        require(namespace.matches(NamespacePattern)) {
            "Namespace $namespace must match regex ${NamespacePattern.pattern}"
        }
    }

    override fun toString(): String = namespace
}
