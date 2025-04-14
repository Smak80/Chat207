package ru.smak.chat

import java.net.Socket
import java.util.Scanner
import kotlin.concurrent.thread

class Client(
    val host: String,
    val port: Int,
) {

    private var userScanner = Scanner(System.`in`)
    private val communicator = Communicator(Socket(host, port))

    init {
        communicator.start(::parse)

        thread {
            while (communicator.isRunnig){
                val userData = userScanner.nextLine()
                if (userData.isNotBlank()){
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