package quest.laxla.mockoge.core.loader

import okio.BufferedSource

public abstract class BundleLoader(fileType: String) {
    public val fileExtension: String = BundleFileExtension + fileType

    public abstract fun load(file: BufferedSource): BundleData?
}
