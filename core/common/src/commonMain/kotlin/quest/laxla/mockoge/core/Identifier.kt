package quest.laxla.mockoge.core

public data class Identifier internal constructor(val namespace: String, val path: String) {
    public val brokenPath: Sequence<String> get() = path.splitToSequence('/')
    public val brokenNamespace: Sequence<String> get() = namespace.splitToSequence('/')

    public override fun toString(): String = namespace + SEPARATOR + path

    public companion object {
        public const val SEPARATOR: Char = ':'

        public val regex: Regex = "[a-z_]+(/[a-z_]+)*".toRegex()
        public val fullRegex: Regex = (regex.pattern + SEPARATOR + regex.pattern).toRegex()
    }
}

