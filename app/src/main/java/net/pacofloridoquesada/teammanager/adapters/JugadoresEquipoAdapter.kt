package net.pacofloridoquesada.teammanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.ItemJugadorBinding
import net.pacofloridoquesada.teammanager.databinding.ItemJugadorConGolesBinding
import net.pacofloridoquesada.teammanager.model.Player

class JugadoresEquipoAdapter : RecyclerView.Adapter<JugadoresEquipoAdapter.JugadoresEquipoViewHolder>() {

    var listaPlayer: List<Player>? = null
    var onJugadorClickListener: OnJugadorClickListener? = null
    var storageRef: StorageReference = FirebaseStorage.getInstance().reference

    fun setLista(lista: List<Player>) {
        listaPlayer = lista
        notifyDataSetChanged()
    }

    inner class JugadoresEquipoViewHolder(val binding: ItemJugadorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val jugador = listaPlayer?.get(this.adapterPosition)
                onJugadorClickListener?.onJugadorClick(jugador)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadoresEquipoViewHolder {
        val binding = ItemJugadorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JugadoresEquipoViewHolder(binding)
    }

    override fun getItemCount(): Int = listaPlayer?.size ?: 0

    override fun onBindViewHolder(holder: JugadoresEquipoViewHolder, position: Int) {
        with(holder){
            with(listaPlayer!!.get(position)) {
                binding.tvNombreJugador.text = name
                if (image != null) {
                    val imageRef = storageRef.child("image").child(image!!)

                    Glide.with(binding.cvImagenJugador.context)
                        .load(imageRef)
                        .error(R.drawable.ic_logo_app)
                        .into(binding.ivJugador)
                }
            }
        }
    }
}