package quest.laxla.mockoge

import kotlin.jvm.JvmInline

public sealed interface Identifiable {
    public val namespace: String?
    public val path: String

    @JvmInline
    public value class Arbitrary(override val path: String) : Identifiable {
        override val namespace: Nothing? get() = null
    }
}