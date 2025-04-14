package ru.smak.chat

import java.net.Socket

class ConnectedClient(val socket: Socket) {

    private val communicator = Communicator(socket)
    private val userName: String? = null

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
            if (echo || it != this) it.communicator.sendMessage(message)
        }
    }

    companion object {
        private val connectedClients = mutableListOf<ConnectedClient>()
    }
}