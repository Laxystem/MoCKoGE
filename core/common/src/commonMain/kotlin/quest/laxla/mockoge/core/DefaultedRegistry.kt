package quest.laxla.mockoge.core

/**
 * @param defaultEntryPath the path of the [defaultEntryIdentifier].
 * @property default the default entry of this Registry.
 * Usage before registration is safe.
 */
public abstract class DefaultedRegistry<T : Any>(
    public val default: T,
    private val defaultEntryPath: String = "root",
    freezerProvider: RegistryFreezerConsumer = RootRegistry::consumeFreezer
) : Registry<T>(freezerProvider), RegistrationAware {

    /**
     * The [Identifier] of the [default entry][DefaultedRegistry.default].
     *
     * Do not use before registration.
     */
    public lateinit var defaultEntryIdentifier: Identifier
        private set

    public fun getOrDefault(identifier: Identifier): T = this[identifier] ?: default

    override fun onRegister(identifier: Identifier) {
        defaultEntryIdentifier = Identifier(identifier.namespace, defaultEntryPath)
        this[defaultEntryIdentifier] = default
    }
}
