package quest.laxla.mockoge.core.loader

/**
 * A [BundleScript] that is bundled (fatjar-ed) with the engine.
 */
public class BundleEntrypoint(
    public val namespace: String,
    public val entrypoint: BundleScript.() -> Unit
): BundleScript() {

}