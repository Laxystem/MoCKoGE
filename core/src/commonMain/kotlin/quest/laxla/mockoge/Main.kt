package quest.laxla.mockoge

import kotlinx.coroutines.runBlocking

public fun main(args: Array<String>): Unit = runBlocking {
    programArguments = args
    platformMain()
    MoCKoGE.init()
}

public lateinit var programArguments: Array<String>
    private set

public expect suspend fun platformMain()
