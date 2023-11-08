package quest.laxla.mockoge.util

import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.constraints.Constraint
import io.github.z4kn4fein.semver.constraints.toConstraint
import quest.laxla.mockoge.loader.BundleDSL

/**
 * Fancy DSL for creating [Version] and [Constraint]s,
 * packaged in an object in order not to infect [String]'s namespace.
 *
 * TODO: convert this to a namespace/inlined object
 *
 * @author Laxystem
 * @since 0.0.1
 */
@BundleDSL
public object VersionDSL {
    /**
     * Accepts any [Version].
     *
     * @since 0.0.1
     * @author Laxystem
     */
    public val any: Constraint = constraint { "*" }

    /**
     * Inclusive or, accepts version A, B, or both.
     *
     * To be used with [ConstraintBlock].
     *
     * TODO: move this into [ConstraintBlock]? Compiler doesn't seem to recognize it there.
     *
     * @author Laxystem
     * @since 0.0.1
     */
    @Suppress("StructuralWrap") // it's a to-do line, can't.
    public infix fun String.or(other: String): String = "$this | $other"

    /**
     * Easily usable fancy [Constraint] creation DSL.
     *
     * @author Laxystem
     * @since 0.0.1
     */
    @BundleDSL
    public fun interface ConstraintBlock {
        /**
         * The functional interface's function.
         *
         * TODO: change to use context()
         *
         * @author Laxystem
         * @since 0.0.1
         */
        public operator fun VersionDSL.invoke(): String

        /**
         * Parses the result of the [invoke] function into a [Constraint].
         */
        public fun parse(): Constraint = VersionDSL().toConstraint()
    }
}

/**
 * Creates a [Constraint] that only accepts this exact [Version].
 *
 * @author Laxystem
 * @since 0.0.1
 */
public fun Version.toExactConstraint(): Constraint = "=$this".toConstraint()

/**
 * Creates a [Constraint] that accepts any version of the same major and minor.
 *
 * @author Laxystem
 * @since 0.0.1
 */
public fun Version.toApproximateConstraint(): Constraint = "~$this".toConstraint()

/**
 * Creates a [VersionDSL.ConstraintBlock] and parses it.
 * @author Laxystem
 * @since 0.0.1
 */
public fun constraint(block: VersionDSL.ConstraintBlock): Constraint = block.parse()