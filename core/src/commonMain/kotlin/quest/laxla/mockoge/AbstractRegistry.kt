package quest.laxla.mockoge

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import quest.laxla.mockoge.util.launchIn

/**
 * Simple implementation of [Registry].
 *
 * @since 0.0.1
 * @author Laxystem
 */
public abstract class AbstractRegistry<T> : Registry<T> where T : Any {
    private val contents = mutableMapOf<Identifier, T>()
    private val registrationEvents = mutableListOf<RegistrationEvent<T>>()

    final override val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    final override fun iterator(): Iterator<Registry.Entry<T>> =
        contents.asSequence().map { (identifier, entry) -> Entry(identifier, entry) }.iterator()

    final override fun register(identifier: Identifier, entry: T): Registry.Entry<T>? =
        if (isMutable && identifier !in this && entry !in this && isValid(identifier, entry)) {
            contents[identifier] = entry

            Entry(identifier, entry).also { wrappedEntry ->
                registrationEvents.asFlow().onEach { it(wrappedEntry) }.launchIn(this)
            }
        } else null

    final override fun get(identifier: Identifier): T? = contents[identifier]

    final override fun getAndWrap(identifier: Identifier): Registry.Entry<T>? =
        this[identifier]?.let { Entry(identifier, it) }

    final override fun get(entry: T): Identifier? = contents.asSequence().find { (_, value) -> value == entry }?.key

    final override fun getAndWrap(entry: T): Registry.Entry<T>? = this[entry]?.let { Entry(it, entry) }

    final override fun wrap(identifier: Identifier): Registry.Key<T> = Key(identifier)

    final override fun contains(entry: T): Boolean = contents.containsValue(entry)

    final override fun contains(identifier: Identifier): Boolean = contents.containsKey(identifier)

    final override fun forEachObserving(event: RegistrationEvent<T>) {
        registrationEvents += event
        asFlow().onEach { event(it) }.launchIn(this)
    }

    final override fun toPersistentMap(): PersistentMap<Identifier, T> = contents.toPersistentMap()

    final override fun toImmutableMap(): ImmutableMap<Identifier, T> = contents.toImmutableMap()

    override fun toString(): String = this::class.qualifiedName ?: this::class.simpleName ?: "unknown registry"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AbstractRegistry<*>

        if (contents != other.contents) return false
        if (registrationEvents != other.registrationEvents) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contents.hashCode()
        result = 31 * result + registrationEvents.hashCode()
        return result
    }

    /**
     * @author Laxystem
     */
    private abstract inner class InnerReservable : Registry.Holder.Reserved<T> {
        override val registry: Registry<T> get() = this@AbstractRegistry
    }

    /**
     * @author Laxystem
     */
    private inner class Entry(
        override val identifier: Identifier, override val value: T
    ) : InnerReservable(), Registry.Entry<T> {

        /**
         * @author Laxystem
         */
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as AbstractRegistry<*>.Entry

            if (identifier != other.identifier) return false
            if (value != other.value) return false

            return true
        }

        /**
         * @author Laxystem
         */
        override fun hashCode(): Int {
            var result = identifier.hashCode()
            result = 31 * result + value.hashCode()
            return result
        }

        /**
         * @author Laxystem
         */
        override fun toString(): String = "$identifier at $registry: $value"
    }

    /**
     * @author Laxystem
     */
    private inner class Key(override val identifier: Identifier) : InnerReservable(), Registry.Key<T> {

        /**
         * @author Laxystem
         */
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as AbstractRegistry<*>.Key

            return identifier == other.identifier
        }

        /**
         * @author Laxystem
         */
        override fun hashCode(): Int {
            return identifier.hashCode()
        }

        /**
         * @author Laxystem
         */
        override fun toString(): String {
            return "$identifier at $registry"
        }
    }
}
