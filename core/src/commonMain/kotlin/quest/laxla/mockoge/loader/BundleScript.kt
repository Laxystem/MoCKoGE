package quest.laxla.mockoge.loader

import io.github.z4kn4fein.semver.constraints.Constraint
import io.github.z4kn4fein.semver.constraints.toConstraint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import quest.laxla.mockoge.*
import quest.laxla.mockoge.Bundle.Relation.*
import quest.laxla.mockoge.util.*
import quest.laxla.mockoge.util.VersionDSL.any
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSerializableLambda
import kotlin.jvm.JvmSynthetic

private typealias RelationDataBuilder = Pair<String, Bundle.Relation>
private typealias Registrar<T> = Pair<Identifiable, Registry<T>>

/**
 * The DSL that creates bundles!
 *
 * @sample BundleScript.Companion.example
 * @author Laxystem
 * @since 0.0.1
 */
@BundleDSL
@Suppress(Deprecation)
public abstract class BundleScript {
    private val _dependencies = mutableListOf<RelationData>()

    /**
     * @author Laxystem
     * @since 0.0.1
     */
    public val dependencies: Sequence<RelationData> get() = _dependencies.asSequence()

    private val _declarations = mutableMapOf<Identifiable, MutableList<Pair<Identifiable, Any>>>()

    /**
     * @author Laxystem
     * @since 0.0.1
     */
    public val declarations: ImmutableMap<Identifiable, ImmutableList<Pair<Identifiable, Any>>> get() = _declarations.toImmutableMap()

    protected val projectVersion: String = throw NotImplementedError("Should've been replaced by the Bundler, check your gradle build")

    /**
     * Adds [namespace] as a dependency.
     *
     * ***Warning:** [version] must be specified, otherwise, the dependency will be ignored.*
     *
     * @author Laxystem
     * @since 0.0.1-alpha
     */
    protected infix fun Bundle.Relation.dependency(namespace: String): RelationDataBuilder =
        namespace to this

    /**
     * Adds the mockoge module [namespace] as a dependency.
     *
     * Equivalent to using [dependency] with `mockoge/` prefixed.
     *
     * ***Warning:** [version] must be specified, otherwise, the dependency will be ignored.*
     *
     * @author Laxystem
     * @since 0.0.1
     */
    protected infix fun Bundle.Relation.officialDependency(namespace: String): RelationDataBuilder =
        dependency(namespace = EngineName + Identifier.NAMESPACE_SEPARATOR + namespace)

    /**
     * Specifies the version of a dependency.
     *
     * @author Laxystem
     * @since 0.0.1
     */
    protected infix fun RelationDataBuilder.version(version: Constraint): BundleReference {
        val (namespace, relation) = this

        _dependencies += RelationData(namespace, relation, version)

        return BundleReference(namespace)
    }

    /**
     * Specifies the version of a dependency.
     *
     * @author Laxystem
     * @since 0.0.1
     */
    protected infix fun RelationDataBuilder.version(version: String): BundleReference = version(version.toConstraint())

    /**
     * Specifies the version of a dependency.
     *
     * @author Laxystem
     * @since 0.0.1
     * @see VersionDSL
     * @see any
     */
    protected inline infix fun RelationDataBuilder.version(version: ConstraintBlock): BundleReference =
        version(VersionDSL(version))


    /**
     * Registers to [a dependency][namespace]'s registries.
     *
     * @author Laxystem
     * @since 0.0.1
     */
    @OptIn(ExperimentalContracts::class)
    protected inline fun namespace(namespace: BundleReference, block: RegistrationScript.() -> Unit) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        RegistrationScript(namespace).block()
    }

    /**
     * Registers to a dependency's registries.
     *
     * @author Laxystem
     * @since 0.0.1
     */
    @OptIn(ExperimentalContracts::class)
    @JvmName("namespace\$infix")
    protected inline infix fun BundleReference.namespace(block: RegistrationScript.() -> Unit) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        namespace(this, block)
    }

    private fun register(path: String, entry: Any, registry: Identifiable): Identifiable =
        Identifiable.Arbitrary(path).also { _declarations.getOrFill(registry) += it to entry }

    /**
     * Registers a [Registry] under [path]
     *
     * @author Laxystem
     * @since 0.0.1
     */
    protected infix fun <T : Any> Registry<T>.aka(path: String): Registrar<T> =
        register(path, entry = this, RootRegistry.identifier) to this

    /**
     * Registers to a [Registry] that was just registered.
     *
     * @author Laxystem
     * @since 0.0.1
     */
    @OptIn(ExperimentalContracts::class)
    protected infix fun <T : Any> Registrar<T>.contains(block: RegistrarScript<T>.() -> Unit) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        val (identifier, _) = this

        RegistrarScript<T>(identifier).block()
    }

    @BundleDSL
    protected inner class RegistrationScript @PublishedApi internal constructor(private val registryNamespace: BundleReference) {
        /**
         * Adds an [entry] to a [registry][quest.laxla.mockoge.Registry].
         *
         * @author Laxystem
         * @receiver Registry [path][quest.laxla.mockoge.Identifier.path]
         * @since 0.0.1
         */
        public operator fun String.set(path: String, entry: Any) {
            this@BundleScript.register(path, entry, registry = this at registryNamespace)
        }
    }

    @BundleDSL
    protected inner class RegistrarScript<T> internal constructor(public val registry: Identifiable) where T : Any {

        /**
         * Registers the receiver at [path].
         *
         * @author Laxystem
         * @since 0.0.1-
         */
        public infix fun T.at(path: String) {
            this@BundleScript.register(path, entry = this, registry)
        }
    }

    public companion object {
        @Suppress(Unreachable, Unused, UnusedVariable)
        private fun BundleScript.example() {
            Extension dependency "graphics" version any // registration forbidden for extensions
            Optional dependency "my_tile_lib" version "8" namespace {
                // register something to my_tile_lib:tilings under the identifier my_namespace:hexagon
                "tilings"["hexagon"] = error("Create a hexagon tiling")
            }

            Incompatible dependency "my_tile_lib/hexagon" version "=4.2 || >=4.5.7 <5.*"

            // dependency on mockoge/graphics
            val graphicsLib = Required officialDependency "graphics" version { "=0.0.1-alpha" or "=0.0.1-alpha.1" }

            (error("Create a registry") as Registry<Any>) aka "bundle_loader" contains {
                error("Create a bundle loader") at "my_custom_bundle_loader"
            }

            namespace(graphicsLib) {
                "renderer"["experimental/qpu"] = error("Emulate a Quantum Processing Unit hehehehehehe")
            }
        }
    }
}

