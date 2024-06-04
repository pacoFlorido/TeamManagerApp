package net.pacofloridoquesada.teammanager.model

data class Player(
    val id: Int?,
    val alias: String,
    val birthday: String,
    val name: String,
    val nationality: String,
    val playerReport: PlayerReport?,
    val user: String
)