package quest.laxla.mockoge.core.util

import kotlinx.collections.immutable.ImmutableList
import okio.FileSystem
import okio.Path

public typealias FileSystems = ImmutableList<FileAccessPoint>
public typealias FileAction<T> = FileSystem.(baseDirectory: Path) -> T

internal expect val fileSystems: FileSystems

internal expect val primaryFileSystem: FileAccessPoint

public class FileAccessPoint(public val fileSystem: FileSystem, baseDirectory: Path) {
    public val baseDirectory: Path = fileSystem.canonicalize(baseDirectory)

    public inline operator fun <T> invoke(action: FileAction<T>): T = fileSystem.action(baseDirectory)
}

public inline fun <T> FileSystems.firstMatchFor(action: FileAction<T?>): T? =
    asSequence().firstNotNullOfOrNull { it(action) }

public inline fun <T> FileSystems.allMatchesFor(crossinline action: FileAction<T?>): Sequence<T> =
    asSequence().mapNotNull { it(action) }

public inline fun <T> FileSystems.allMatchesFor(crossinline action: FileAction<Sequence<T>>): Sequence<T> =
    asSequence().flatMap { it(action) }
