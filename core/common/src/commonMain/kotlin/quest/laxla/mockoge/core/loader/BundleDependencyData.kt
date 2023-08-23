package quest.laxla.mockoge.core.loader

import io.github.z4kn4fein.semver.constraints.Constraint
import kotlinx.serialization.Serializable
import quest.laxla.mockoge.core.Bundle

@Serializable
public data class BundleDependencyData(val relation: Bundle.BundleDependencyRelation, val constraints: List<Constraint>)
