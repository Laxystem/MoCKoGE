package quest.laxla.mockoge.core.util

import kotlinx.collections.immutable.ImmutableList
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import quest.laxla.mockoge.core.MoCKoGE
import kotlin.jvm.JvmName

public typealias FileAction<T> = FileSystem.() -> T

internal expect val FileSystem: FileSystem

/**
 * Secondary, read only filesystems to find bundles in.
 */
internal expect val SecondaryFileSystems: ImmutableList<FileSystem>

public val CurrentDirectory: Path = ".".toPath()

public val BasePath: Path = if (MoCKoGE.isDevelopmentEnvironment) "out".toPath() else CurrentDirectory

public inline fun <T> Iterable<FileSystem>.firstMatchFor(action: FileAction<T?>): T? = firstNotNullOfOrNull(action)

public fun <T> Iterable<FileSystem>.allMatchesFor(action: FileAction<T?>): Sequence<T> = asSequence().mapNotNull(action)

@JvmName("flattenAllMatchesFor")
public fun <T> Iterable<FileSystem>.allMatchesFor(action: FileAction<Sequence<T>>): Sequence<T> = asSequence().flatMap(action)

