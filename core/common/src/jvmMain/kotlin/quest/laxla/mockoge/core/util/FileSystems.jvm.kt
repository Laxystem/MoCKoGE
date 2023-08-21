package quest.laxla.mockoge.core.util

import kotlinx.collections.immutable.persistentListOf
import okio.FileSystem
import okio.Path.Companion.toPath

internal actual val primaryFileSystem: FileAccessPoint = FileAccessPoint(FileSystem.SYSTEM, System.getProperty("user.dir").toPath())
@Suppress("unused")
internal actual val fileSystems: FileSystems = persistentListOf(FileAccessPoint(FileSystem.RESOURCES, ".".toPath()), primaryFileSystem)
