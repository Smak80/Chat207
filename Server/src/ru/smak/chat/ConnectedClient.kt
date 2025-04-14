package ru.smak.chat

import java.net.Socket

class ConnectedClient(val socket: Socket) {

    private val communicator = Communicator(socket)

    init{
        communicator.start{ parse(it) }
    }

    private fun parse(message: String){
        communicator.sendMessage(message)
    }

    fun stop(){
        communicator.stop()
    }
}