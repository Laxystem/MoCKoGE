package quest.laxla.mockoge

/**
 * Simple, easily creatable registry.
 *
 * @author Laxystem
 * @since 0.0.1
 */
public open class SimpleRegistry<T>(public override val root: Registry.Root) : AbstractRegistry<T>(),
    Registry.Rooted<T> where T : Any {

    override val isMutable: Boolean
        get() = root.isMutable

    override val identifier: Identifier
        get() = root.getOrThrow(this)

    override fun toString(): String = root[this]?.toString() ?: super.toString()
}
