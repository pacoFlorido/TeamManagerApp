package net.pacofloridoquesada.teammanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.model.Trainer
import net.pacofloridoquesada.teammanager.network.NetworkService

class TeamManagerViewModel : ViewModel() {
    fun addPlayer(player: Player){
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkService.teamManagerService.createPlayer(player)
            Log.i("Response: ", response.errorBody().toString())
        }
    }

    fun addTrainer(trainer: Trainer){
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkService.teamManagerService.createTrainer(trainer)
            Log.i("Response: ", response.errorBody().toString())
        }
    }
}