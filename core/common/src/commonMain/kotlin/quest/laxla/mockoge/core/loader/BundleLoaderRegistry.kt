package quest.laxla.mockoge.core.loader

import quest.laxla.mockoge.core.DefaultedRegistry

public object BundleLoaderRegistry : DefaultedRegistry<BundleLoader>(JsonBundleLoader, defaultEntryPath = "json")