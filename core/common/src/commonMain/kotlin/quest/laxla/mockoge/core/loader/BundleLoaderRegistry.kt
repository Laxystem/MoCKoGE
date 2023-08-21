package quest.laxla.mockoge.core.loader

import quest.laxla.mockoge.core.Registry
import quest.laxla.mockoge.core.RootRegistry

public object BundleLoaderRegistry : Registry<BundleLoader>(RootRegistry::consumeFreezer)
