package quest.laxla.mockoge.util

import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.constraints.Constraint
import io.github.z4kn4fein.semver.constraints.satisfiedBy

public interface DependencyRelation {
    public val behaviour: Behaviour

    public enum class Behaviour {
        Incompatible, Optional, Required
    }
}

public data class DependencyVersion(val version: Version, val iteration: Int) : Comparable<DependencyVersion> {
    override fun compareTo(other: DependencyVersion): Int {
        return iteration.compareTo(other.iteration).takeUnless { it == 0 } ?: version.compareTo(other.version)
    }
}

public fun resolveDependencyIteration(constraints: Sequence<Constraint>, version: Version): DependencyVersion? {
    constraints.forEachIndexed { index, dependencyLayer ->
        if (dependencyLayer satisfiedBy version) return DependencyVersion(version, index)
    }

    return null
}

public fun resolveDependencyVersion(versions: Sequence<DependencyVersion>): DependencyVersion? = versions.sorted().firstOrNull()

