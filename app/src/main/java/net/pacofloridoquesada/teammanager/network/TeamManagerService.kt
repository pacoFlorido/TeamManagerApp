package net.pacofloridoquesada.teammanager.network

import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.model.Team
import net.pacofloridoquesada.teammanager.model.Trainer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TeamManagerService {
    @POST("player")
    suspend fun createPlayer(@Body player: Player) : Response<Player>

    @POST("trainer")
    suspend fun createTrainer(@Body trainer: Trainer) : Response<Trainer>

    @POST("team")
    suspend fun createTeam(@Body team: Team) : Response<Team>
}