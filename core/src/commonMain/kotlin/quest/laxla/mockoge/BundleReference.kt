package quest.laxla.mockoge

/**
 * Represents a bundle that may or may not exist during runtime;
 * Is provided by declaring an *optional dependency* on said bundle.
 *
 * @see NamespaceProvider
 */
public class BundleReference(namespace: String) : NamespaceProvider(namespace)