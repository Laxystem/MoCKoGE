package quest.laxla.mockoge.loader

import io.github.z4kn4fein.semver.constraints.Constraint
import quest.laxla.mockoge.Bundle

public data class RelationData(
    val namespace: String,
    val relation: Bundle.Relation,
    val versions: Constraint
)
