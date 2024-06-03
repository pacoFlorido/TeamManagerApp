package net.pacofloridoquesada.teammanager.ui.main.clasificacion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.network.NetworkService

class ClasificacionViewModel : ViewModel() {

    private val _jugadoresEquipo = MutableLiveData<MutableList<Player>?>()
    val jugadoresEquipo: LiveData<MutableList<Player>?> get() = _jugadoresEquipo

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

    fun getPlayersByIdTeamOrderBy(idTeam: Int, orden: String){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.getPlayersByTeamOrderBy(idTeam, orden)
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
}