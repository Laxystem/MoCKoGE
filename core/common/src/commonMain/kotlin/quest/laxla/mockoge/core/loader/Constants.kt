@file:JvmName("LoaderConstants")

package quest.laxla.mockoge.core.loader

import kotlin.jvm.JvmField
import kotlin.jvm.JvmName

public const val BundleFileExtension: String = ".bundle"

@JvmField public val FileExtensionPattern: Regex = "(\\.[a-z]+)+".toRegex()
