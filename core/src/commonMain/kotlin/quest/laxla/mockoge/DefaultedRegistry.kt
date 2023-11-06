package quest.laxla.mockoge

/**
 * @param defaultEntryPath the path of the [defaultEntryIdentifier].
 * @property default the default entry of this Registry.
 * Usage before registration is safe.
 */
public abstract class DefaultedRegistry<T : Any>(
    public val default: T,
    lifecycle: FreezingListener = RootRegistry.lifecycle
) : Registry<T>(lifecycle) {

    public val defaultIdentifier: Identifier by lazy { get(default)!! }

    public fun getOrDefault(identifier: Identifier): T = this[identifier] ?: default
}
