package quest.laxla.mockoge.core

public object RootRegistry : DefaultedRegistry<Registry<*>>(RootRegistry) {
    private val freezers = mutableListOf<(Boolean) -> Unit>()

    internal var isRegistryFrozen: Boolean
        get() = isFrozen
        set(value) = freezers.forEach { it(value) }

    public fun consumeFreezer(freezer: RegistryFreezer) {
        freezers += freezer
    }
}
