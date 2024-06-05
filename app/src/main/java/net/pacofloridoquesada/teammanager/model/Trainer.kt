package net.pacofloridoquesada.teammanager.model

data class Trainer(
    val birthday: String,
    val id: Int?,
    var name: String,
    val nationality: String,
    val user: String,
    var image: String?
)