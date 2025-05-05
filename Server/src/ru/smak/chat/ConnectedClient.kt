package ru.smak.chat

import kotlinx.coroutines.*
import java.nio.channels.AsynchronousSocketChannel

class ConnectedClient(socket: AsynchronousSocketChannel) {

    private val communicator = Communicator(socket)
    private val userName: String? = null
    private val clientScope = CoroutineScope(Dispatchers.IO)

    init{
        connectedClients.add(this)
        communicator.start{ parse(it) }
    }

    private fun parse(message: String){
        sendToAll(message, echo = false)
    }

    fun stop(){
        communicator.stop()
    }

    fun sendToAll(message: String, echo: Boolean = true){
        connectedClients.forEach {
            if (echo || it != this) clientScope.launch{ it.communicator.sendMessage(message)}
        }
    }

    companion object {
        private val connectedClients = mutableListOf<ConnectedClient>()
    }
}