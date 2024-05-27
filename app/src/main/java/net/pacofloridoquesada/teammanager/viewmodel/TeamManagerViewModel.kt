package net.pacofloridoquesada.teammanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.network.NetworkService

class TeamManagerViewModel : ViewModel() {
    fun addPlayer(player: Player){
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkService.TeamManagerService.createPlayer(player)
        }
    }
}