package server

import com.google.gson.Gson
import domain.TicTacToeManager
import messages.ServerToClientMessage
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class GameThread(private val socket: Socket) : Thread() {

    private val gson = Gson()

    override fun run() {
        try {
            PrintWriter(socket.getOutputStream(), true).use { output ->
                BufferedReader(InputStreamReader(socket.getInputStream())).use { input ->
                    var inputLine: String?
                    var outputLine: String
                    val gameManager = TicTacToeManager()

                    outputLine = gameManager.processInput(null)
                    output.println(outputLine)

                    while (input.readLine().also { inputLine = it } != null) {
                        outputLine = gameManager.processInput(inputLine)
                        output.println(outputLine)

                        val message = gson.fromJson(outputLine, ServerToClientMessage::class.java)
                        if (message.gameFinished) {
                            message.run {
                                println("Game with $gameId finished.")
                                winner?.let {
                                    println("${it.name} won.")
                                }
                            }
                            break
                        }
                    }
                    socket.close()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}