package messages

data class ClientToServerMessage(
    val row: Int,
    val column: Int
)
