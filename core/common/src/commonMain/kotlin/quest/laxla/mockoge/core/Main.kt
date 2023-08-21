package quest.laxla.mockoge.core

import kotlinx.coroutines.runBlocking

public fun main(args: Array<String>): Unit = runBlocking {
    programArguments = args
    MoCKoGE.init()
}

public lateinit var programArguments: Array<String>
    private set
