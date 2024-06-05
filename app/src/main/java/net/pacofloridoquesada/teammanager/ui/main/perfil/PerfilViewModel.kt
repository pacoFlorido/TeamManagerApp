package net.pacofloridoquesada.teammanager.ui.main.perfil

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.model.Trainer
import net.pacofloridoquesada.teammanager.network.NetworkService

class PerfilViewModel : ViewModel() {

    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?> get() = _player

    private val _trainer = MutableLiveData<Trainer?>()
    val trainer: LiveData<Trainer?> get() = _trainer

    fun getPlayer(user: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.getPlayerByUser(user)
                if (response.isSuccessful) {
                    Log.i("Response", "Jugador: ${response.body()}")
                    _player.postValue(response.body())
                } else {
                    Log.e("Error", "Error obteniendo jugador, ${response.errorBody()}")
                }
            }
        } catch (e: Exception) {
            Log.e("Exception: ", "${e.message}")
        }
    }

    fun getTrainer(user: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.getTrainerByUser(user)
                if (response.isSuccessful) {
                    Log.i("Response", "Entrenador: ${response.body()}")
                    _trainer.postValue(response.body())
                } else {
                    Log.e("Error", "Error obteniendo entrenador, ${response.errorBody()}")
                }
            }
        } catch (e: Exception) {
            Log.e("Exception: ", "${e.message}")
        }
    }

    fun updatePlayerRecord(player : Player){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.updatePlayer(player)
                if (response.isSuccessful) {
                    Log.i("Response", "Jugador actualizado: ${response.body()}")
                    _player.postValue(response.body())
                } else {
                    Log.e("Error", "Error obteniendo jugador actualizado, ${response.errorBody()}")
                }
            }
        } catch (e: Exception) {
            Log.e("Exception: ", "${e.message}")
        }
    }

    fun updateTrainer(trainer: Trainer){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.updateTrainer(trainer)
                if (response.isSuccessful) {
                    Log.i("Response", "Jugador actualizado: ${response.body()}")
                    _trainer.postValue(response.body())
                } else {
                    Log.e("Error", "Error obteniendo jugador actualizado, ${response.errorBody()}")
                }
            }
        } catch (e: Exception) {
            Log.e("Exception: ", "${e.message}")
        }
    }

    fun delteUserFromTeam(user: String){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.deleteUserFromTeam(user)
                if (response.isSuccessful) {
                    Log.i("Response", "Usuario abandono el equipo: ${response.body()}")
                } else {
                    Log.e("Error", "Error obteniendo equipo actualizado, ${response.errorBody()}")
                }
            }
        } catch (e: Exception) {
            Log.e("Exception: ", "${e.message}")
        }
    }
}