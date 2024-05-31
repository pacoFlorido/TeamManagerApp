package net.pacofloridoquesada.teammanager.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.model.Team
import net.pacofloridoquesada.teammanager.model.Trainer
import net.pacofloridoquesada.teammanager.network.NetworkService

class TeamManagerViewModel : ViewModel() {

    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?> get() = _player

    private val _trainer = MutableLiveData<Trainer?>()
    val trainer: LiveData<Trainer?> get() = _trainer

    private val _team = MutableLiveData<Team?>()
    val team: LiveData<Team?> get() = _team

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

    fun getUser(user: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = NetworkService.teamManagerService.getPlayerByUser(user)
                if (response.isSuccessful){
                    val playerr = response.body()

                    if (playerr != null){
                        _player.postValue(playerr)
                    } else {
                        val response2 = NetworkService.teamManagerService.getTrainerByUser(user)
                        if (response2.isSuccessful) {
                            val trainerr = response2.body()

                            if (trainerr != null){
                                _trainer.postValue(trainerr)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Response", "Exception: ${e.message}")
            }
        }
    }

    fun getTeamByUser(user: String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkService.teamManagerService.getTeamByUser(user)
            if (response.isSuccessful){
                Log.i("Response", "Team getted: ${response.body()}")
                _team.postValue(response.body())
            }
        }
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