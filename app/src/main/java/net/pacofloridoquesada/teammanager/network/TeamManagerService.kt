package net.pacofloridoquesada.teammanager.network

import net.pacofloridoquesada.teammanager.model.Event
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.model.Team
import net.pacofloridoquesada.teammanager.model.Trainer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TeamManagerService {
    @POST("player")
    suspend fun createPlayer(@Body player: Player) : Response<Player>

    @POST("trainer")
    suspend fun createTrainer(@Body trainer: Trainer) : Response<Trainer>

    @POST("team")
    suspend fun createTeam(@Body team: Team) : Response<Team>

    @GET("player/{user}")
    suspend fun getPlayerByUser(@Path("user") user: String) : Response<Player>

    @GET("trainer/{user}")
    suspend fun getTrainerByUser(@Path("user") user: String) : Response<Trainer>

    @DELETE("player/delete/{user}")
    suspend fun deletePlayerByUser(@Path("user") user: String)

    @DELETE("trainer/delete/{user}")
    suspend fun deleteTrainerByUser(@Path("user") user: String)

    @PUT("team/update/addUser/{user}")
    suspend fun addUserToTeam(@Body team: Team, @Path("user") user: String) : Response<Team>

    @GET("team/getBy/{user}")
    suspend fun getTeamByUser(@Path("user") user: String) : Response<Team>

    @GET("team/getNextEvent/{idTeam}")
    suspend fun getNextEvent(@Path("idTeam") idTeam: Int) : Response<Event>

}