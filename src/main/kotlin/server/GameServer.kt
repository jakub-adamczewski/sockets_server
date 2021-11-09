package server

import java.io.IOException
import java.net.ServerSocket
import kotlin.system.exitProcess

object GameServer {
    @JvmStatic
    fun main(args: Array<String>) {

        if (args.size != 1) {
            System.err.println("Usage: kotlin server.GameServer <port number>")
            exitProcess(1)
        }

        val portNumber = args[0].toInt()

        println("Game server started.")

        try {
            while (true) {
                ServerSocket(portNumber).use { serverSocket ->
                    GameThread(serverSocket.accept()).start()
                }
            }
        } catch (e: IOException) {
            System.err.println("Could not listen on port $portNumber")
            exitProcess(-1)
        }
    }
}
