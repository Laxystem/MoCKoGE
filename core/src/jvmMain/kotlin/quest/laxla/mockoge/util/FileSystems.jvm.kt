@file:Suppress("UNRESOLVED_REFERENCE") // fixme: https://youtrack.jetbrains.com/issue/KTIJ-14471
package quest.laxla.mockoge.util

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import okio.FileSystem

@Suppress("UNINITIALIZED_VARIABLE", "ACTUAL_WITHOUT_EXPECT") // fixme: https://youtrack.jetbrains.com/issue/KTIJ-14471
internal actual val FileSystem: FileSystem = FileSystem.SYSTEM

@Suppress("UNINITIALIZED_VARIABLE", "ACTUAL_WITHOUT_EXPECT") // fixme: https://youtrack.jetbrains.com/issue/KTIJ-14471
internal actual val SecondaryFileSystems: ImmutableList<FileSystem> = persistentListOf(FileSystem.RESOURCES)
