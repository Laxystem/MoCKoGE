package quest.laxla.mockoge.core.util

import kotlinx.collections.immutable.persistentListOf
import okio.FileSystem
import okio.Path.Companion.toPath

internal actual val primaryFileSystem: FileAccessPoint = FileAccessPoint(FileSystem.SYSTEM, "~/.mockoge".toPath())

@Suppress("unused")
internal actual val fileSystems: FileSystems = persistentListOf(primaryFileSystem)
