package data

import data.Player

data class ServerToClientMessage(
    val row: Int?,
    val column: Int?,
    val gameFinished: Boolean,
    val winner: Player?,
    val gameId: Int
)
