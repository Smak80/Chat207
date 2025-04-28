package ru.smak.chat

import java.io.PrintWriter
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class Communicator(
    val socket: AsynchronousSocketChannel,
) {
    private var parse: ((String)->Unit)? = null
    var isRunnig = false
        private set
    //private val scanner = Scanner(socket.getInputStream())
    //private val writer = PrintWriter(socket.getOutputStream())

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

    suspend fun sendMessage(message: String){
        val ba = message.toByteArray()
        val buf = ByteBuffer.allocate(ba.size + Int.SIZE_BYTES)
        buf.putInt(ba.size)
        buf.put(ba)
        buf.flip()
        val wrote = suspendCoroutine{
            socket.write(
                buf,
                null,
                ActionCompletionHandler(it)
            )
        }
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