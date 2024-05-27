package net.pacofloridoquesada.teammanager.model

data class Player(
    val alias: String,
    val birthday: String,
    val id: Int,
    val name: String,
    val nationality: String,
    val playerReport: PlayerReport,
    val user: String
)