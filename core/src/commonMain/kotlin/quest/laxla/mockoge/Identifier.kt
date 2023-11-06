package quest.laxla.mockoge

public infix fun String.at(namespace: NamespaceProvider): Identifier = Identifier(namespace, this)

public class Identifier : Identifiable {
    public override val namespace: String
    public override val path: String

    public val brokenPath: Sequence<String> get() = path.splitToSequence('/')
    public val brokenNamespace: Sequence<String> get() = namespace.splitToSequence('/')

    public constructor(namespace: NamespaceProvider, path: String) : this(namespace.namespace, path)

    private constructor(namespace: String, path: String) {
        require(PathPattern matches namespace) { "Namespace ($namespace) doesn't match regex (${PathPattern.pattern})" }
        require(NamespacePattern matches path) { "Path ($path) doesn't match regex (${NamespacePattern.pattern})" }

        this.namespace = namespace
        this.path = path
    }

    // TODO: decide the fate of this constructor
    private constructor(identifier: String) {
        val pattern = IdentifierPattern.matchEntire(identifier)

        requireNotNull(pattern) { "Identifier string ($identifier) doesn't match regex ($IdentifierPattern)" }

        namespace = pattern.groups[NAMESPACE_GROUP]!!.value
        path = pattern.groups[PATH_GROUP]!!.value
    }

    public override fun toString(): String = namespace + SEPARATOR + path

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

    public companion object {
        public const val NAMESPACE_GROUP: String = "namespace"
        public const val PATH_GROUP: String = "path"

        public const val SEPARATOR: Char = ':'
        public const val PATH_SEPARATOR: Char = '/'
        public const val NAMESPACE_SEPARATOR: Char = '/'

        public val PathPattern: Regex = "(?<$PATH_GROUP>[a-z_]+($PATH_SEPARATOR[a-z_]+)*)".toRegex()
        public val NamespacePattern: Regex = "(?<$NAMESPACE_GROUP>[a-z_]+($NAMESPACE_SEPARATOR[a-z_]+)*)".toRegex()
        public val IdentifierPattern: Regex = "${NamespacePattern.pattern}$SEPARATOR${PathPattern.pattern}".toRegex()
    }
}
