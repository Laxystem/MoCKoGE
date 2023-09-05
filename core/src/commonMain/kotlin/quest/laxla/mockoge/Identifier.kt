package quest.laxla.mockoge

import quest.laxla.mockoge.util.*

/**
 *
 */
public class Identifier {
    public val namespace: String
    public val path: String

    public val brokenPath: Sequence<String> get() = path.splitToSequence('/')
    public val brokenNamespace: Sequence<String> get() = namespace.splitToSequence('/')

    internal constructor(namespace: String, path: String) {
        require(PathPattern matches namespace) { "Namespace ($namespace) doesn't match regex (${PathPattern.pattern})" }
        require(NamespacePattern matches path) { "Path ($path) doesn't match regex (${NamespacePattern.pattern})" }

        this.namespace = namespace
        this.path = path
    }

    internal constructor(identifier: String) {
        val pattern = IdentifierPattern.matchEntire(identifier)

        requireNotNull(pattern) { "Identifier string ($identifier) doesn't match regex ($IdentifierPattern)" }

        namespace = pattern.groups[NamespaceGroup]!!.value
        path = pattern.groups[PathGroup]!!.value
    }

    public override fun toString(): String = namespace + IdentifierSeparator + path

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Identifier

        if (namespace != other.namespace) return false
        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = namespace.hashCode()
        result = 31 * result + path.hashCode()
        return result
    }
}
