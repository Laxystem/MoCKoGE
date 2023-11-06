package quest.laxla.mockoge.util

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

public fun <K, T> MutableMap<K, MutableList<T>>.getOrFill(key: K): MutableList<T> =
    getOrPut(key, ::mutableListOf)

public fun <K, T> MutableMap<K, MutableList<T>>.toImmutableMap(): ImmutableMap<K, ImmutableList<T>> =
    asSequence().map { (key, entries) -> key to entries.toImmutableList() }.toMap().toImmutableMap()