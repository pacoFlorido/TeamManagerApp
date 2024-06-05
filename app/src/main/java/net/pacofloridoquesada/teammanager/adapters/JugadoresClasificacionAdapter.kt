package net.pacofloridoquesada.teammanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.ItemJugadorEstadisticasBinding
import net.pacofloridoquesada.teammanager.model.Player

class JugadoresClasificacionAdapter : RecyclerView.Adapter<JugadoresClasificacionAdapter.JugadoresClasificacionViewHolder>() {

    var listaPlayer: List<Player>? = null
    var onJugadorClickListener: OnJugadorClickListener? = null
    var storageRef: StorageReference = FirebaseStorage.getInstance().reference

    fun setListaOrdenada(lista: List<Player>) {
        listaPlayer = lista

        notifyDataSetChanged()
    }

    inner class JugadoresClasificacionViewHolder(val binding: ItemJugadorEstadisticasBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val jugador = listaPlayer?.get(this.adapterPosition)
                onJugadorClickListener?.onJugadorClick(jugador)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadoresClasificacionViewHolder {
        val binding = ItemJugadorEstadisticasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JugadoresClasificacionViewHolder(binding)
    }

    override fun getItemCount(): Int = listaPlayer?.size ?: 0

    override fun onBindViewHolder(holder: JugadoresClasificacionViewHolder, position: Int) {
        with(holder){
            with(listaPlayer!!.get(position)) {
                binding.tvNombreJugador.text = name
                binding.tvGolesEstadisticas.text = playerReport!!.goals.toString()
                binding.tvAsistenciasEstadisticas.text = playerReport.assists.toString()
                binding.tvPartidosEstadisticas.text = playerReport.matches.toString()
                binding.tvAmarillasEstadisticas.text = playerReport.yellowCards.toString()
                binding.tvRojasEstadisticas.text = playerReport.redCards.toString()
                if (image != null) {
                    val imageRef = storageRef.child("image").child(image!!)

                    Glide.with(binding.cvItemJugadorEstats.context)
                        .load(imageRef)
                        .error(R.drawable.ic_logo_app)
                        .into(binding.ivJugador)
                }
            }
        }
    }


}