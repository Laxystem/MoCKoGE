package quest.laxla.mockoge.core.loader

public abstract class BundleScript {
    private val _dependencies = mutableListOf<DependencyData>()
    public val dependencies: Sequence<DependencyData> get() = _dependencies.asSequence()

    public fun dependencies(block: DependenciesScript.() -> Unit) = DependenciesScript(_dependencies).block()
}


private fun bundleScript() = BundleEntrypoint("mockoge") {

}