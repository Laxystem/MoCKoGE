package quest.laxla.mockoge.core.loader

import kotlin.jvm.JvmInline

@JvmInline
public value class RelationsScript(private val dependencies: MutableList<RelationData>) {
    public infix fun relatesTo(bundleNamespace: String): RelationData.Builder = RelationData.Builder(bundleNamespace)
    // todo: finish creating bundlescript
}