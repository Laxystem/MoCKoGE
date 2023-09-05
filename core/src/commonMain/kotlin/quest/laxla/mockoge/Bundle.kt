package quest.laxla.mockoge

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import quest.laxla.mockoge.util.DependencyRelation

/**
 * Represents a bundle that has to exist during runtime;
 * An instance is provided to all bundles, and additional ones can be provided by declaring a *required dependency*.
 *
 * Allows for more functionality than the [BundleReference].
 *
 * TODO: links
 *
 * @see NamespaceProvider
 */
public class Bundle internal constructor(namespace: String) : NamespaceProvider(namespace) {
    internal val logger: KLogger = KotlinLogging.logger(namespace)

    @Serializable
    public enum class BundleDependencyRelation(override val behaviour: DependencyRelation.Behaviour?) : DependencyRelation {
        Incompatible(DependencyRelation.Behaviour.Incompatible),
        Optional(DependencyRelation.Behaviour.Optional),
        Extension(DependencyRelation.Behaviour.Optional),
        Required(DependencyRelation.Behaviour.Required)
    }
}
