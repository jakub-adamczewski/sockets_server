package server.domain

import data.Player
import data.Position
import kotlin.math.abs

class TicTacToeBoard(private val gameId: Int) {

    init {
        println("New game started, game id: $gameId.")
    }

    private val boardState = mutableMapOf<Position, Player?>(
        Position(0, 0) to null,
        Position(0, 1) to null,
        Position(0, 2) to null,
        Position(1, 0) to null,
        Position(1, 1) to null,
        Position(1, 2) to null,
        Position(2, 0) to null,
        Position(2, 1) to null,
        Position(2, 2) to null,
    )

    private val winConditions = listOf<(position: Position) -> Boolean>(
        { it.row == 0 },
        { it.row == 1 },
        { it.row == 2 },
        { it.column == 0 },
        { it.column == 1 },
        { it.column == 2 },
        { it.row == it.column },
        { (it.row == 1 && it.column == 1) || abs(it.row - it.column) == 2 },
    )

    val gameFinished: Boolean
        get() = boardState.none { it.value == null } || winner != null

    val winner: Player?
        get() = winConditions
            .map { condition -> winnerFor(condition) }
            .firstOrNull { it != null }

    private fun winnerFor(condition: (position: Position) -> Boolean): Player? = boardState
        .filterKeys { condition(it) }
        .map { it.value }
        .toList()
        .distinct()
        .let { playersInLine ->
            if (playersInLine.size == 1) {
                playersInLine.first()
            } else {
                null
            }
        }

    fun pickRandomFreeField(player: Player): Position? = boardState.run {
        filterValues { it == null }
            .map { it.key }
            .randomOrNull()
            .let { randomFreePosition ->
                randomFreePosition?.let {
                    set(it, player)
                    logPick(player, it)
                    it
                }

            }
    }

    fun pickField(position: Position, player: Player): Position? = boardState.run {
        filterValues { it == null }
            .map { it.key }
            .let { freePositions ->
                if (position in freePositions) {
                    set(position, player)
                    logPick(player, position)
                    position
                } else {
                    null
                }
            }
    }

    private fun logPick(player: Player, position: Position) {
        println("-------------------------------------------------")
        println("Game id: $gameId.")
        println("${player.name} picked $position.")
        println("Board state:")
        printBoard()
        println()
    }

    private fun printBoard() {
        for (row in 0..2) {
            for (column in 0..2) {
                print("${boardState[Position(row, column)].toBoardString()} ")
            }
            println()
        }
    }

    private fun Player?.toBoardString(): String = when (this) {
        Player.CLIENT -> "C"
        Player.SERVER -> "S"
        null -> "#"
    }
}