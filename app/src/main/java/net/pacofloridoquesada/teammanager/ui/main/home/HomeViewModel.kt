package net.pacofloridoquesada.teammanager.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.pacofloridoquesada.teammanager.model.Event
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.network.NetworkService

class HomeViewModel : ViewModel() {

    private val _nextEvent = MutableLiveData<Event?>()
    val nextEvent: LiveData<Event?> get() = _nextEvent

    private val _pichichi = MutableLiveData<Player?>()
    val pichichi: LiveData<Player?> get() = _pichichi

    private val _conMasGoles50 = MutableLiveData<MutableList<Player>?>()
    val conMasGoles50: LiveData<MutableList<Player>?> get() = _conMasGoles50



    fun getNextEvent(user: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.getTeamByUser(user)
                if (response.isSuccessful) {
                    val team = response.body()
                    Log.i("Response", "Equipo obtenido: ${team.toString()}")

                    team?.let {
                        val response2 = NetworkService.teamManagerService.getNextEvent(team.id!!)
                        if (response2.isSuccessful) {
                            Log.i("Response", "Evento obtenido: ${response2.body()}")
                            _nextEvent.postValue(response2.body())
                        } else {
                            Log.e("Response", "Error getting event: ${response2.errorBody()}")
                        }
                    } ?: run {
                        Log.e("Response", "Error: team is null")
                    }
                } else {
                    Log.e("Response", "Error obteniendo evento: ${response.errorBody()}")
                }
            }
        } catch (e: Exception){
            Log.e("Exception: ", "${e.message}" )
        }
    }

    fun get50ConMasGolesDeLaApp() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.get50WithMoreGoals()
                if (response.isSuccessful){
                    _conMasGoles50.postValue(response.body())
                } else{
                    Log.e("Response", "Error obteniendo jugadores: ${response.errorBody()}")
                }
            }
        } catch (e: Exception){
            Log.e("Exception: ", "${e.message}" )
        }
    }
}