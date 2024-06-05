package net.pacofloridoquesada.teammanager.network

import net.pacofloridoquesada.teammanager.model.Event
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.model.Team
import net.pacofloridoquesada.teammanager.model.Trainer
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @GET("player/get50withMoreGoals")
    suspend fun get50WithMoreGoals() : Response<MutableList<Player>>

    @GET("player/player-list/{idTeam}")
    suspend fun getPlayersOfTeam(@Path("idTeam") idTeam: Int) : Response<MutableList<Player>>

    @GET("trainer/trainer-list/{idTeam}")
    suspend fun getTrainersOfTeam(@Path("idTeam") idTeam: Int) : Response<MutableList<Trainer>>

    @GET("team/{code}")
    suspend fun getTeamByCode(@Path("code") code: String) : Response<Team>

    @GET("team/getNextEvents/{idTeam}")
    suspend fun getNextEventsOfTeam(@Path("idTeam") idTeam: Int) : Response<MutableList<Event>>

    @POST("team/event/{user}")
    suspend fun createEvent(@Path("user") user: String, @Body event: Event) : Response<Event>

    @PUT("team/event")
    suspend fun updateEvent(@Body event: Event) : Response<Event>

    @GET("team/getEventById/{idEvento}")
    suspend fun getEventById(@Path("idEvento") idEvent: Int) : Response<Event>

    @GET("player/player-list/{idTeam}/{orden}")
    suspend fun getPlayersByTeamOrderBy(@Path("idTeam") idTeam: Int, @Path("orden") orden: String) : Response<MutableList<Player>>

    @PUT("player/update")
    suspend fun updatePlayer(@Body player: Player) : Response<Player>

    @PUT("trainer/update")
    suspend fun updateTrainer(@Body trainer: Trainer) : Response<Trainer>

    @PUT("team/update")
    suspend fun updateTeam(@Body team: Team) : Response<Team>

    @PATCH("team/deleteUserFromTeam/{user}")
    suspend fun deleteUserFromTeam(@Path("user") user: String) : Response<Team>
}