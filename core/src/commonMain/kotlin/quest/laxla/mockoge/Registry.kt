package quest.laxla.mockoge

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.PersistentMap
import quest.laxla.mockoge.util.Lifecycle
import quest.laxla.mockoge.util.UnusedProperty
import quest.laxla.mockoge.util.UnusedPublic
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName

public typealias RegistrationEvent<T> = suspend (Registry.Entry<T>) -> Unit

public interface Registry<T> : Iterable<Registry.Entry<T>>, Lifecycle where T : Any {
    /**
     * Does this [Registry] currently accept new entries?
     *
     * @since 0.0.1
     */
    public val isMutable: Boolean

    /**
     * Registers [entry] to this [Registry] under [identifier].
     *
     * May fail and return `null` if any of the following is true:
     *  - this registry is not currently [mutable][isMutable]
     *  - there's already an entry registered under this [identifier]
     *  - this [entry] is already registered under a different [Identifier]
     *  - [isValid] returned false
     *
     * @return `null` if registration failed.
     * @since 0.0.1
     */
    public fun register(identifier: Identifier, entry: T): Entry<T>?

    /**
     * @since 0.0.1
     */
    public fun isValid(identifier: Identifier, entry: T): Boolean = true

    /**
     * Retrieves the entry registered under [identifier].
     *
     * @since 0.0.1
     */
    public operator fun get(identifier: Identifier): T?

    /**
     * Retrieves the entry registered under [identifier] and wraps it.
     *
     * @see get
     * @see Entry
     * @since 0.0.1
     */
    public fun getAndWrap(identifier: Identifier): Entry<T>?

    /**
     * @since 0.0.1
     * @see Key
     */
    public fun wrap(identifier: Identifier): Key<T>

    /**
     * Retrieves the [Identifier] this [entry] is registered under.
     *
     * @since 0.0.1
     */
    public operator fun get(entry: T): Identifier?

    /**
     * Retrieves the [Identifier] this [entry] is registered under and wraps it.
     *
     * @see Entry
     * @see get
     * @since 0.0.1
     */
    public fun getAndWrap(entry: T): Entry<T>?

    /**
     * Is [entry] registered to this [Registry]?
     *
     * @since 0.0.1
     */
    public operator fun contains(entry: T): Boolean

    /**
     * Is an entry registered under [identifier]?
     *
     * @since 0.0.1
     */
    public operator fun contains(identifier: Identifier): Boolean

    /**
     * Runs [event] on all current and future registry entries.
     *
     * @since 0.0.1
     */
    public fun forEachObserving(event: RegistrationEvent<T>)

    /**
     * @since 0.0.1
     */
    public fun toPersistentMap(): PersistentMap<Identifier, T>

    /**
     * @since 0.0.1
     */
    public fun toImmutableMap(): ImmutableMap<Identifier, T>


    /**
     * Registry of registries.
     *
     * One registry to rule them all, one registry to find them,
     * one registry to bring them all and in the darkness bind them.
     *
     * @see Rooted
     */
    public interface Root : Registry<Rooted<*>>

    /**
     * A registry that is registered to a [root] registry.
     * @see Root
     */
    public interface Rooted<T> : Registry<T> where T : Any {
        /**
         * The registry that this registry is registered to.
         * @see Root
         */
        public val root: Root

        /**
         * The [Identifier] this registry is registered under.
         *
         * @since 0.0.1
         */
        public val identifier: Identifier
    }

    /**
     * Holds a registry entry.
     *
     * @since 0.0.1
     */
    public sealed interface Holder<T> where T : Any {
        /**
         * @since 0.0.1
         */
        public val value: T?

        /**
         * A [Holder] that knows its [registry].
         *
         * @since 0.0.1
         */
        public sealed interface Reserved<T> : Holder<T> where T : Any {

            /**
             * The [Registry] this entry is registered to.
             *
             * @since 0.0.1
             */
            public val registry: Registry<T>
        }

        /**
         * A [Holder] that knows its [identifier].
         *
         * @since 0.0.1
         */
        public sealed interface Identified<T> : Holder<T>, Comparable<Identified<T>> where T : Any {

            /**
             * The [Identifier] this entry is registered under.
             *
             * @since 0.0.1
             */
            public val identifier: Identifier

            override fun compareTo(other: Identified<T>): Int = identifier.compareTo(other.identifier)
        }

        /**
         * A [Holder] that directly references its [value].
         *
         * @since 0.0.1
         */
        public sealed interface Direct<T> : Holder<T> where T : Any {
            override val value: T
        }

        /**
         * A [Holder] containing an arbitrary [value].
         *
         * @since 0.0.1
         */
        @JvmInline
        public value class Arbitrary<T : Any>(override val value: T) : Direct<T>
    }

    /**
     * Wraps an [identifier] and a [registry].
     *
     * @since 0.0.1
     */
    public interface Key<T> : Holder.Reserved<T>, Holder.Identified<T> where T : Any {
        override val value: T?
            get() = registry[identifier]
    }

    /**
     * Wraps an [entry][value], its [identifier] and the [registry] it's registered to.
     *
     * Instances of this interface are *guaranteed* to be registered,
     * as they can only be created using [Registry.getAndWrap] and [Registry.register].
     */
    public interface Entry<T> : Key<T>, Holder.Direct<T> where T : Any {
        override val value: T
    }
}

/**
 * Indicates an entry doesn't exist in the [registry].
 *
 * @since 0.0.1
 * @author Laxystem
 */
@Suppress(UnusedPublic, UnusedProperty)
public class NoSuchEntryException public constructor(
    message: String, public val registry: Registry<*>
) : NoSuchElementException("[$registry]: $message") {

    /**
     * @author Laxystem
     * @since 0.0.1
     */
    public constructor(
        identifier: Identifier,
        registry: Registry<*>
    ) : this("No entry is registered under identifier [$identifier]", registry)

    /**
     * @author Laxystem
     * @since 0.0.1
     */
    public constructor(
        entry: Any,
        registry: Registry<*>
    ) : this("Entry [$entry] isn't registered to this registry", registry)
}

/**
 * Is there an entry registered under [identifier][Registry.Key.identifier] to [registry][Registry.Key.registry]?
 *
 * @since 0.0.1
 * @author Laxystem
 */
public val Registry.Key<*>.isRegistered: Boolean get() = identifier in registry

/**
 * @since 0.0.1
 * @author Laxystem
 * @see Registry.Holder.Reserved.registry
 */
public val <T> Registry.Holder<T>.registryOrNull: Registry<T>? where T : Any
    get() = (this as? Registry.Holder.Reserved)?.registry

/**
 * @since 0.0.1
 * @author Laxystem
 * @see Registry.Holder.Identified.identifier
 */
public val Registry.Holder<*>.identifierOrNull: Identifier?
    get() = (this as? Registry.Holder.Identified)?.identifier

/**
 * Creates a [Sequence] instance
 * that returns the [identifier][Registry.Entry.identifier]s of this [Registry]'s entries when being iterated.
 *
 * @since 0.0.1
 * @see Identifier
 * @author Laxystem
 */
public fun <T> Registry<T>.asIdentifierSequence(): Sequence<Identifier> where T : Any =
    asSequence().map(Registry.Entry<T>::identifier)

/**
 * Register [identifier] and [value], throwing on failure.
 *
 * @see Registry.register
 * @author Laxystem
 * @since 0.0.1
 */
public operator fun <T> Registry<T>.set(identifier: Identifier, value: T) where T : Any {
    register(identifier, value)
        ?: error("Cannot register [$value] to [$this] under [$identifier]; It may already be registered")
}

/**
 * Retrieves the entry registered under [identifier], throwing if it is not found.
 *
 * @since 0.0.1
 * @see Registry.get
 * @author Laxystem
 */
public fun <T> Registry<T>.getOrThrow(identifier: Identifier): T where T : Any =
    this[identifier] ?: throw NoSuchEntryException(identifier, this)

/**
 * Retrieves the entry registered under [identifier] and wraps it, throwing it is not found.
 *
 * @since 0.0.1
 * @see Registry.getAndWrap
 * @author Laxystem
 */
public fun <T> Registry<T>.getAndWrapOrThrow(identifier: Identifier): Registry.Entry<T> where T : Any =
    getAndWrap(identifier) ?: throw NoSuchEntryException(identifier, this)


/**
 * Retrieves the [Identifier] this [entry] is registered under, throwing if it is not found.
 *
 * @since 0.0.1
 * @see Registry.get
 * @author Laxystem
 */
public fun <T> Registry<T>.getOrThrow(entry: T): Identifier where T : Any =
    this[entry] ?: throw NoSuchEntryException(entry, this)

/**
 * Wraps [entry], throwing if it is not registered.
 *
 * @since 0.0.1
 * @see Registry.getAndWrap
 * @author Laxystem
 */
public fun <T> Registry<T>.getAndWrapOrThrow(entry: T): Registry.Entry<T> where T : Any =
    getAndWrap(entry) ?: throw NoSuchEntryException(entry, this)

/**
 * Runs [event] on all current and future registry entries.
 *
 * @author Laxystem
 * @since 0.0.1
 * @see Registry.forEachObserving
 */
@JvmName("forEachIdentifierObserving")
public inline fun <T> Registry<T>.forEachObserving(crossinline event: (Identifier) -> Unit) where T : Any {
    forEachObserving { (_, identifier) -> event(identifier) }
}

/**
 * Runs [event] on all current and future registry entries.
 *
 * @author Laxystem
 * @since 0.0.1
 * @see Registry.forEachObserving
 */
public inline fun <T> Registry<T>.forEachObserving(crossinline event: (T) -> Unit) where T : Any {
    forEachObserving { (value) -> event(value) }
}

/**
 * Is this registry registered to the [root][Registry.Rooted.root] registry?
 *
 * @since 0.0.1
 * @author Laxystem
 */
public val <T> Registry.Rooted<T>.isRegistered: Boolean where T : Any get() = identifier in root

/**
 * Wraps this registry as an entry of [root][Registry.Rooted.root].
 *
 * @since 0.0.1
 * @author Laxystem
 */
public val <T> Registry.Rooted<T>.asEntry: Registry.Entry<Registry.Rooted<*>> where T : Any
    get() = root.getAndWrapOrThrow(identifier)

/**
 * @author Laxystem
 * @since 0.0.1
 * @see Registry.Holder.value
 */
public operator fun <T> Registry.Holder<T>.component1(): T? where T : Any = value

/**
 * @author Laxystem
 * @since 0.0.1
 * @see Registry.Holder.value
 */
public operator fun <T> Registry.Holder.Direct<T>.component1(): T where T : Any = value

/**
 * @author Laxystem
 * @since 0.0.1
 * @see Registry.Holder.identifierOrNull
 */
public operator fun Registry.Holder<*>.component2(): Identifier? = identifierOrNull

/**
 * @author Laxystem
 * @since 0.0.1
 * @see Registry.Holder.Identified.identifier
 */
public operator fun Registry.Holder.Identified<*>.component2(): Identifier = identifier

/**
 * @author Laxystem
 * @since 0.0.1
 * @see Registry.Holder.registryOrNull
 */
public operator fun <T> Registry.Holder<T>.component3(): Registry<T>? where T : Any = registryOrNull

/**
 * @author Laxystem
 * @since 0.0.1
 * @see Registry.Holder.Reserved.registry
 */
public operator fun <T> Registry.Holder.Reserved<T>.component3(): Registry<T> where T : Any = registry
