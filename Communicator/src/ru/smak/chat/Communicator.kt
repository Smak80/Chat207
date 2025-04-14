package ru.smak.chat

import java.io.PrintWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class Communicator(
    val socket: Socket,
) {
    private var parse: ((String)->Unit)? = null
    var isRunnig = false
        private set
    private val scanner = Scanner(socket.getInputStream())
    private val writer = PrintWriter(socket.getOutputStream())

    private fun startMessageAccepting(){
        thread {
            while(isRunnig){
                try {
                    val data = scanner.nextLine()
                    parse?.invoke(data)
                } catch (_: Throwable){
                    break
                }
            }
        }
    }

    fun sendMessage(message: String){
        writer.println(message)
        writer.flush()
    }

    fun start(parser: (String)->Unit){
        if (socket.isClosed) throw Exception("Client disconnected")
        parse = parser
        if (!isRunnig) {
            isRunnig = true
            startMessageAccepting()
        }
    }

    fun stop(){
        isRunnig = false
        socket.close()
    }
}