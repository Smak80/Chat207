package ru.smak.chat

import java.io.PrintWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class ConnectedClient(val socket: Socket) {

    private var isRunnig = true
    private val scanner = Scanner(socket.getInputStream())
    private val writer = PrintWriter(socket.getOutputStream())

    init{
        startMessageAccepting()
    }

    private fun startMessageAccepting(){
        thread {
            while(isRunnig){
                try {
                    val data = scanner.nextLine()
                    parse(data)
                } catch (_: Throwable){
                    break
                }
            }
            socket.close()
        }
    }

    private fun parse(message: String){
        println("Клиент прислал: $message")
        sendMessage(message)
    }

    fun sendMessage(message: String){
        writer.println(message)
        writer.flush()
    }

    fun stop(){
        isRunnig = false
    }
}