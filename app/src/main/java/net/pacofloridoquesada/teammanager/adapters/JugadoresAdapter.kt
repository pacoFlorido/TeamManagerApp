package net.pacofloridoquesada.teammanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.pacofloridoquesada.teammanager.databinding.ItemJugadorBinding
import net.pacofloridoquesada.teammanager.databinding.ItemJugadorConGolesBinding
import net.pacofloridoquesada.teammanager.model.Player

class JugadoresHomeAdapter : RecyclerView.Adapter<JugadoresHomeAdapter.JugadoresHomeViewHolder>() {

    var lista50Player: List<Player>? = null

    fun setLista50(lista: List<Player>) {
        lista50Player = lista
        notifyDataSetChanged()
    }

    inner class JugadoresHomeViewHolder(val binding: ItemJugadorConGolesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadoresHomeViewHolder {
        val binding = ItemJugadorConGolesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JugadoresHomeViewHolder(binding)
    }

    override fun getItemCount(): Int = lista50Player?.size ?: 0

    override fun onBindViewHolder(holder: JugadoresHomeViewHolder, position: Int) {
        with(holder){
            with(lista50Player!!.get(position)) {
                binding.tvNombreJugador.text = name
                binding.tvGoles.text = playerReport?.goals.toString()
            }
        }
    }
}