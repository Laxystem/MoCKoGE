package quest.laxla.mockoge

import quest.laxla.mockoge.util.at


private val freezers = mutableListOf<Registry.Freezer>()

/**
 * All registries are registered to this [Registry], that is also registered to itself.
 *
 * *One [Registry] to rule them [all][Iterable.all], One [Registry] to [find][Iterable.find] them,
 * One [Registry] to bring them all and in the darkness bind them.*
 */
public object RootRegistry : Registry<Registry<*>>({ freezers += freezers }) {

    /**
     * Delegates to [isFrozen]. Allows internal code to manage the [RootRegistry]'s [lifecycle].
     */
    internal var isRegistryFrozen: Boolean
        get() = isFrozen
        set(value) = freezers.forEach { it.setFrozen(value) }


    init {
        this["root" at MoCKoGE] = this
    }
}
