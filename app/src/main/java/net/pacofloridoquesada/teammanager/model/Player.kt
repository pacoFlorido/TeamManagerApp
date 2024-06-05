package net.pacofloridoquesada.teammanager.model

data class Player(
    var alias: String,
    val birthday: String,
    var name: String,
    val nationality: String,
    val playerReport: PlayerReport?,
    val user: String
)