package quest.laxla.mockoge.core.loader

import io.github.z4kn4fein.semver.constraints.Constraint
import kotlinx.collections.immutable.ImmutableList
import quest.laxla.mockoge.core.Bundle

public data class RelationData(
    val namespace: String,
    val relation: Bundle.BundleDependencyRelation,
    val versions: ImmutableList<Constraint>
) {
    public data class Builder(
        val namespace: String,
        var relation: Bundle.BundleDependencyRelation = Bundle.BundleDependencyRelation.Required,
        val versions: MutableList<Constraint> = mutableListOf()
    )
}
