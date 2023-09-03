package quest.laxla.mockoge.core.loader

public abstract class BundleScript {
    private val _dependencies = mutableListOf<RelationData>()
    public val dependencies: Sequence<RelationData> get() = _dependencies.asSequence()

    public fun dependencies(block: RelationsScript.() -> Unit): Unit = RelationsScript(_dependencies).block()
}


private fun bundleScript() = BundleEntrypoint("mockoge") {

}