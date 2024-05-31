package net.pacofloridoquesada.teammanager.network

import com.google.gson.GsonBuilder
import net.pacofloridoquesada.teammanager.constantes.Constantes
import net.pacofloridoquesada.teammanager.constantes.NullOnEmptyConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    val teamManagerService: TeamManagerService by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd").create())
            )
            .build().create(TeamManagerService::class.java)
    }
}
