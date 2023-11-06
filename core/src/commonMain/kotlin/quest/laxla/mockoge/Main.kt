package quest.laxla.mockoge

import kotlinx.coroutines.runBlocking
import quest.laxla.mockoge.loader.BundleScript
import quest.laxla.mockoge.loader.Bundleable

public fun main(args: Array<String>): Unit = runBlocking {
    programArguments = args
    MoCKoGE.init()
}

public lateinit var programArguments: Array<String>
    private set
