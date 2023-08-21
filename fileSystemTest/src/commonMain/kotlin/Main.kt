import okio.FileSystem
import okio.Path.Companion.toPath

public fun main() {
    println("Checking filesystems...")
    fileSystems.map { it to it.canonicalize(".".toPath()) }.forEach(::println)
}

public expect val fileSystems: List<FileSystem>
