package domain

import com.google.gson.Gson
import data.Player
import data.Position
import messages.ServerToClientMessage

class TicTacToeManager {

    private val board = TicTacToeBoard()
    private val gson = Gson()

    fun processInput(clientPickedPosition: String?): String = board.run {
        clientPickedPosition?.toIntPair()?.let { (row, column) ->
            pickField(Position(row, column), Player.CLIENT)
        }

        (if (!gameFinished) pickRandomFreeField(Player.SERVER) else null)
            .let {
                ServerToClientMessage(
                    row = it?.row,
                    column = it?.column,
                    gameFinished = gameFinished,
                    winner = winner
                ).let { message ->
                    gson.toJson(message)
                }
            }
    }

    private fun String.toIntPair(): Pair<Int, Int> {
        val numbers = trim().toList().filter { it != ' ' }.map { it.toString().toInt() }
        return numbers[0] to numbers[1]
    }

}