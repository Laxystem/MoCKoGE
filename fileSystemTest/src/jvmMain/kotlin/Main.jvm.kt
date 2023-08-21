import okio.FileSystem

public actual val fileSystems: List<FileSystem>
    get() = listOf(FileSystem.SYSTEM, FileSystem.RESOURCES)