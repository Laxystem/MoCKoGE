package quest.laxla.mockoge.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn

/**
 * An object that can be [close]d, and supports [coroutineScope].
 * 
 * @since 0.0.1
 */
@OptIn(ExperimentalStdlibApi::class)
public interface Lifecycle : AutoCloseable {
    /**
     * @since 0.0.1
     */
    public val coroutineScope: CoroutineScope

    override fun close() {
        coroutineScope.cancel("Lifecycle [$this] of type [${this::class.qualifiedName}] was closed")
    }
}

/**
 * @see launchIn
 * @author Laxystem
 * @since 0.0.1
 */
public fun <T> Flow<T>.launchIn(lifecycle: Lifecycle): Job = launchIn(lifecycle.coroutineScope)
