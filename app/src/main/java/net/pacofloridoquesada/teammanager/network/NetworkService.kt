package net.pacofloridoquesada.teammanager.network

import com.google.gson.GsonBuilder
import net.pacofloridoquesada.teammanager.constantes.Constantes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd")
        .create()
    val TeamManagerService = Retrofit.Builder()
        .baseUrl(Constantes.BASE_URL)
        ///conversor de JSON, existen otros(XML,csv...consultar ayuda)
        .addConverterFactory(GsonConverterFactory.create(gson))
        //Crea el servicio con los métodos definidos de la interface
        .build().create(TeamManagerService::class.java)
}