package net.pacofloridoquesada.teammanager.model

data class PlayerReport(
    val id: Int,
    var goals: Int,
    var assists: Int,
    var matches: Int,
    var yellowCards: Int,
    var redCards: Int
)