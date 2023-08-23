package quest.laxla.mockoge.core

import kotlin.jvm.JvmInline

public typealias RegistryFreezer = (Boolean) -> Unit
public typealias RegistryFreezerConsumer = (RegistryFreezer) -> Unit

public abstract class Registry<T>(
    freezerProvider: RegistryFreezerConsumer = RootRegistry::consumeFreezer
) : Iterable<Registry.Entry<T>> where T : Any {

    private val contents = mutableMapOf<Identifier, T>()

    /**
     * Does the registry currently allow content modification?
     */
    public var isFrozen: Boolean = false
        private set

    /**
     * The [Identifier] this registry is registered under in the [RootRegistry].
     *
     * Do not call before registration.
     */
    public val identifier: Identifier by lazy {
        RootRegistry[this]
            ?: throw NoSuchRegistryEntryException("Registry [${this::class.qualifiedName}] isn't registered in Root Registry.")
    }

    init {
        freezerProvider {
            isFrozen = it
        }
    }

    public open fun isValid(identifier: Identifier, entry: T): Boolean = true

    public operator fun set(identifier: Identifier, entry: T): Unit? =
        if (!isFrozen && identifier !in this && entry !in this && isValid(identifier, entry)) {
            contents[identifier] = entry
            if (entry is RegistrationAware) entry.onRegister(identifier)

            Unit
        } else null

    public operator fun get(identifier: Identifier): T? = contents[identifier]
    public fun getOrThrow(identifier: Identifier): T =
        this[identifier] ?: entryError("No entry found under identifier [$identifier] in Registry [$this]")

    public operator fun get(entry: T): Identifier? = contents.asSequence().find { (_, value) -> value == entry }?.key
    public fun getOrThrow(entry: T): Identifier =
        this[entry] ?: entryError("Entry [$entry] not found in Registry [$this]")

    public operator fun contains(identifier: Identifier): Boolean = identifier in contents
    public operator fun contains(entry: T): Boolean = contents.containsValue(entry)

    final override fun iterator(): Iterator<Entry<T>> = contents.asSequence().map { Entry(it) }.iterator()
    final override fun toString(): String = identifier.toString()

    /**
     * Represents a [Registry] entry.
     *
     * @param mapEntry the wrapped [Map.Entry]
     * @property identifier the identifier this entry is registered under
     * @property value the value of this entry
     */
    @JvmInline
    public value class Entry<T> internal constructor(private val mapEntry: Map.Entry<Identifier, T>) where T : Any {
        public val identifier: Identifier get() = mapEntry.key
        public val value: T get() = mapEntry.value

        public operator fun component1(): Identifier = identifier
        public operator fun component2(): T = value
    }

    public companion object {
        protected fun entryError(message: String): Nothing = throw NoSuchRegistryEntryException(message)
    }
}
