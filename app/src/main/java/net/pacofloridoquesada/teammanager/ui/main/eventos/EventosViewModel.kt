package net.pacofloridoquesada.teammanager.ui.main.eventos

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

class EventosViewModel : ViewModel() {

    private val _eventos = MutableLiveData<MutableList<Event>?>()
    val eventos: LiveData<MutableList<Event>?> get() = _eventos

    private val _evento = MutableLiveData<Event?>()
    val evento: LiveData<Event?> get() = _evento

    fun getNextEventsByTeam(idTeam: Int) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.getNextEventsOfTeam(idTeam)
                if (response.isSuccessful){
                    Log.i("Response", "Eventos: ${response.body()}")
                    _eventos.postValue(response.body())
                } else{
                    Log.e("Response", "Error obteniendo eventos: ${response.errorBody()}")
                }
            }
        } catch (e: Exception){
            Log.e("Exception: ", "${e.message}" )
        }
    }

    fun createEvent(event: Event, user: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.createEvent(user, event)
                if (response.isSuccessful){
                    Log.i("Response", "Evento creado: ${response.body()}")
                    _evento.postValue(response.body())
                } else{
                    Log.e("Response", "Error obteniendo evento: ${response.errorBody()}")
                }
            }
        } catch (e: Exception){
            Log.e("Exception: ", "${e.message}" )
        }
    }

    fun getEventById(idEvent: Int){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.getEventById(idEvent)
                if (response.isSuccessful){
                    Log.i("Response", "Evento obtenido: ${response.body()}")
                    _evento.postValue(response.body())
                } else{
                    Log.e("Response", "Error obteniendo evento: ${response.errorBody()}")
                }
            }
        } catch (e: Exception){
            Log.e("Exception: ", "${e.message}" )
        }
    }

    fun updateEvent(event: Event) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = NetworkService.teamManagerService.updateEvent(event)
                if (response.isSuccessful){
                    Log.i("Response", "Evento actualizado: ${response.body()}")
                    _evento.postValue(response.body())
                } else{
                    Log.e("Response", "Error obteniendo evento: ${response.errorBody()}")
                }
            }
        } catch (e: Exception){
            Log.e("Exception: ", "${e.message}" )
        }
    }
}