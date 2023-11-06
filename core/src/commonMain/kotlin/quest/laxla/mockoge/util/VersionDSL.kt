package quest.laxla.mockoge.util

import io.github.z4kn4fein.semver.constraints.Constraint
import io.github.z4kn4fein.semver.constraints.toConstraint
import quest.laxla.mockoge.loader.BundleDSL

public typealias ConstraintBlock = VersionDSL.() -> String

@BundleDSL
public object VersionDSL {
    public val any: ConstraintBlock = { "*" }

    public inline operator fun invoke(block: ConstraintBlock): Constraint = this.block().toConstraint()

    public infix fun String.or(other: String): String = "$this | $other"
}