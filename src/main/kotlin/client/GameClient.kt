package client

import com.google.gson.Gson
import messages.ServerToClientMessage
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.UnknownHostException
import kotlin.system.exitProcess

object GameClient {

    @JvmStatic
    fun main(args: Array<String>) {

        if (args.size != 2) {
            System.err.println(
                "Usage: kotlin GameClient <server ip address or host name> <port number>"
            )
            exitProcess(1)
        }

        val hostName = args[0]
        val portNumber = args[1].toInt()
        val gson = Gson()

        try {
            Socket(hostName, portNumber).use { socket ->
                PrintWriter(socket.getOutputStream(), true).use { output ->
                    BufferedReader(InputStreamReader(socket.getInputStream())).use { input ->

                        val stdIn = BufferedReader(InputStreamReader(System.`in`))
                        var fromServer: String
                        var fromUser: String?

                        while (input.readLine().also { fromServer = it } != null) {
                            println("Server: $fromServer")

                            val message = gson.fromJson(fromServer, ServerToClientMessage::class.java)
                            if (message.gameFinished) {
                                println("Game finished, ${message.winner} won.")
                                break
                            }

                            fromUser = stdIn.readLine()
                            fromUser?.let { output.println(it) }
                        }
                    }
                }
            }
        } catch (e: UnknownHostException) {
            System.err.println("Don't know about host $hostName")
            exitProcess(1)
        } catch (e: IOException) {
            System.err.println("Couldn't get I/O for the connection to $hostName")
            exitProcess(1)
        }
    }
}