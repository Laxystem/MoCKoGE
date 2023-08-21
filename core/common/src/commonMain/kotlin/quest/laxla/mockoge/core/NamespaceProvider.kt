package quest.laxla.mockoge.core

/**
 * Represents an object that is capable of generating [Identifier]s of the given [namespace].
 *
 * @property namespace the [Identifier.namespace] of the [Identifier] this provider can create.
 * @see [at]
 */
public sealed class NamespaceProvider(public val namespace: String) {
    init {
        if (!namespace.matches(Identifier.regex))
            throw IllegalArgumentException("Namespace $namespace must match regex [${Identifier.regex}]")
    }
}
