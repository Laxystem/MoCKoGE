package quest.laxla.mockoge.core.util

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import okio.FileSystem

internal actual val FileSystem: FileSystem = FileSystem.SYSTEM

internal actual val SecondaryFileSystems: ImmutableList<FileSystem> = persistentListOf(FileSystem.RESOURCES)
