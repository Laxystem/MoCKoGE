package quest.laxla.mockoge

import kotlin.jvm.JvmInline

public sealed interface Identifiable : Comparable<Identifiable> {
    public val namespace: String?
    public val path: String

    override fun compareTo(other: Identifiable): Int = compareValues(namespace, other.namespace).let {
        if (it == 0) path.compareTo(other.path) else it
    }

    @JvmInline
    public value class Arbitrary(override val path: String) : Identifiable {
        override val namespace: Nothing? get() = null
    }
}
