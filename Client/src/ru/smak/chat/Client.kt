package ru.smak.chat

import kotlinx.coroutines.*
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousSocketChannel
import java.util.Scanner
import kotlin.coroutines.suspendCoroutine

class Client(
    val host: String,
    val port: Int,
) {

    private var userScanner = Scanner(System.`in`)
    private val socket = AsynchronousSocketChannel.open()
    private val communicator = Communicator (socket)
    private val clientScope = CoroutineScope(Dispatchers.IO)

    init {
        runBlocking {
            suspendCoroutine {
                socket.connect(
                    InetSocketAddress(host, port),
                    null,
                    ActionCompletionHandler(it)
                )
            }
            communicator.start(::parse)
            while (communicator.isRunnig) {
                val userData = userScanner.nextLine()
                if (userData.isNotBlank()) {
                    communicator.sendMessage(userData)
                } else {
                    stop()
                }
            }
        }
    }

    private fun parse(message: String){
        println(message)
    }

    fun stop(){
        communicator.stop()
    }
}