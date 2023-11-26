package quest.laxla.mockoge

/**
 * The client-side top-level [root][Registry.Root] [registry][Registry].
 */
public object RootRegistry : AbstractRegistry<Registry.Rooted<*>>(), Registry.Root {
    override var isMutable: Boolean = false
        internal set
}
