package ru.smak.chat

import kotlinx.coroutines.*
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousServerSocketChannel
import kotlin.coroutines.suspendCoroutine

class Server(
    port: Int = 5207
) {

    val serverSocket = AsynchronousServerSocketChannel.open()
    private val serverScope = CoroutineScope(Dispatchers.IO)

    init{

        serverSocket.bind(InetSocketAddress(port))

        runBlocking {
            while(true){
                val socket = suspendCoroutine {
                    serverSocket.accept(
                        null,
                        ActionCompletionHandler(it)
                    )
                }
                ConnectedClient(socket)
            }
            serverSocket.close()
        }
    }
}