package domain

import data.Player
import data.Position
import kotlin.math.abs

class TicTacToeBoard {

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
                    println("pickedRandom: ${boardState.forEach { elem -> println(elem.toPair()) }}")
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
                    println("pickedField: ${boardState.forEach { println(it.toPair()) }}")
                    position
                } else {
                    null
                }
            }
    }

}