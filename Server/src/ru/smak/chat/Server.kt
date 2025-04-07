package ru.smak.chat

import java.io.PrintWriter
import java.net.ServerSocket
import java.util.*
import kotlin.concurrent.thread

class Server(
    val port: Int = 5207
) {

    val serverSocket = ServerSocket(port)

    init{
        thread {
            while (true) {
                val socket = serverSocket.accept()
                ConnectedClient(socket)
            }
            serverSocket.close()
        }
    }
}