package net.pacofloridoquesada.teammanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.model.Team
import net.pacofloridoquesada.teammanager.model.Trainer
import net.pacofloridoquesada.teammanager.network.NetworkService

class TeamManagerViewModel : ViewModel() {

    private var player: Player? = null
    private var trainer: Trainer? = null
    private var teamm: Team? = null

    fun addPlayer(player: Player) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkService.teamManagerService.createPlayer(player)
            Log.i("Response: ", response.errorBody().toString())
        }
    }

    fun addTrainer(trainer: Trainer) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkService.teamManagerService.createTrainer(trainer)
            Log.i("Response: ", response.errorBody().toString())
        }
    }

    fun addTeam(team: Team, user: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Primera llamada a la API: crear el equipo
                val response = NetworkService.teamManagerService.createTeam(team)
                if (response.isSuccessful) {
                    val teamm = response.body()
                    Log.i("Response", "Team created: $teamm")

                    // Segunda llamada a la API: agregar usuario al equipo
                    teamm?.let {
                        val response2 = NetworkService.teamManagerService.addUserToTeam(teamm, user)
                        if (response2.isSuccessful) {
                            Log.i("Response", "User added to team: ${response2.body()!!.id}")
                        } else {
                            Log.e("Response", "Error adding user to team: ${response2.errorBody()}")
                        }
                    } ?: run {
                        Log.e("Response", "Error: teamm is null")
                    }
                } else {
                    Log.e("Response", "Error creating team: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("Response", "Exception: ${e.message}")
            }
        }
    }

    fun getPlayerByUser(user: String): Player? {
        viewModelScope.launch(Dispatchers.IO) {
            player = NetworkService.teamManagerService.getPlayerByUser(user).body()
        }

        return player
    }

    fun getTrainerByUser(user: String): Trainer? {
        viewModelScope.launch(Dispatchers.IO) {
            val trainer = NetworkService.teamManagerService.getTrainerByUser(user).body()
        }

        return trainer
    }

    fun deletePlayerByUser(user: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkService.teamManagerService.deletePlayerByUser(user)
            Log.i("Response: ", response.toString())
        }
    }

    fun deleteTrainerByUser(user: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkService.teamManagerService.deleteTrainerByUser(user)
            Log.i("Response: ", response.toString())
        }
    }

    fun addUserToTeam(team: Team?, user: String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkService.teamManagerService.addUserToTeam(team!!, user)
            Log.i("Response: ", "hola " + response.errorBody().toString())
        }
    }
}