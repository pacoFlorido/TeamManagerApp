package net.pacofloridoquesada.teammanager.ui.main.equipo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.pacofloridoquesada.teammanager.model.Event
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.model.Trainer
import net.pacofloridoquesada.teammanager.network.NetworkService

class EquipoViewModel : ViewModel() {

    private val _jugadoresEquipo = MutableLiveData<MutableList<Player>?>()
    val jugadoresEquipo: LiveData<MutableList<Player>?> get() = _jugadoresEquipo

    private val _entrenadoresEquipo = MutableLiveData<MutableList<Trainer>?>()
    val entrenadoresEquipo: LiveData<MutableList<Trainer>?> get() = _entrenadoresEquipo

    private val _trainer = MutableLiveData<Trainer?>()
    val trainer: LiveData<Trainer?> get() = _trainer

    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?> get() = _player

    fun getPlayersByIdTeam(idTeam: Int){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.getPlayersOfTeam(idTeam)
                if (response.isSuccessful){
                    Log.i("Response", "Jugadores: ${response.body()}")
                    _jugadoresEquipo.postValue(response.body())
                } else{
                    Log.e("Response", "Error obteniendo jugadores: ${response.errorBody()}")
                }
            }
        } catch (e: Exception){
            Log.e("Exception: ", "${e.message}" )
        }
    }

    fun getTrainersByIdTeam(idTeam: Int){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.getTrainersOfTeam(idTeam)
                if (response.isSuccessful){
                    Log.i("Response", "Entrenadores: ${response.body()}")
                    _entrenadoresEquipo.postValue(response.body())
                } else{
                    Log.e("Response", "Error obteniendo entrenadores: ${response.errorBody()}")
                }
            }
        } catch (e: Exception){
            Log.e("Exception: ", "${e.message}" )
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

}