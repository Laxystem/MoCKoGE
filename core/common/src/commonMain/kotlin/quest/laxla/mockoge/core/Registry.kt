package quest.laxla.mockoge.core

public typealias RegistryFreezer = (Boolean) -> Unit
public typealias RegistryFreezerConsumer = (RegistryFreezer) -> Unit

public abstract class Registry<T : Any>(
    freezerProvider: RegistryFreezerConsumer
) : Iterable<Map.Entry<Identifier, T>> {

    private val contents = mutableMapOf<Identifier, T>()
    public var isFrozen: Boolean = false
        private set

    init {
        freezerProvider {
            isFrozen = it
        }
    }

    public open fun isValid(identifier: Identifier, entry: T): Boolean = true

    public operator fun set(identifier: Identifier, entry: T): Unit? =
        if (!isFrozen && identifier !in this && entry !in this && isValid(identifier, entry)) {
            contents[identifier] = entry
        } else null

    public operator fun get(identifier: Identifier): T? = contents[identifier]

    public operator fun contains(identifier: Identifier): Boolean = identifier in contents
    public operator fun contains(entry: T): Boolean = contents.containsValue(entry)

    final override fun iterator(): Iterator<Map.Entry<Identifier, T>> = contents.asSequence().iterator()
}
