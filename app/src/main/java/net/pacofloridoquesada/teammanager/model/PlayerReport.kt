package net.pacofloridoquesada.teammanager.model

data class PlayerReport(
    val assists: Int,
    val goals: Int,
    val id: Int,
    val matches: Int,
    val redCards: Int,
    val yellowCards: Int
)