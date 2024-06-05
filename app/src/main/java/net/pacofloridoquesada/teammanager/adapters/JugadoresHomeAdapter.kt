package net.pacofloridoquesada.teammanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.ItemJugadorConGolesBinding
import net.pacofloridoquesada.teammanager.model.Player

class JugadoresHomeAdapter : RecyclerView.Adapter<JugadoresHomeAdapter.JugadoresHomeViewHolder>() {

    var listaPlayer: List<Player>? = null
    var storageRef: StorageReference = FirebaseStorage.getInstance().reference

    fun setLista(lista: List<Player>) {
        listaPlayer = lista
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

    override fun getItemCount(): Int = listaPlayer?.size ?: 0

    override fun onBindViewHolder(holder: JugadoresHomeViewHolder, position: Int) {
        with(holder){
            with(listaPlayer!!.get(position)) {
                binding.tvNombreJugador.text = name
                binding.tvGoles.text = playerReport?.goals.toString()

                if (image != null) {
                    Firebase.storage.reference.child("image").child(image!!)
                        .downloadUrl.addOnSuccessListener {
                            Glide.with(binding.cvItemJugadorGoles.context)
                                .load(it)
                                .error(R.drawable.ic_logo_app)
                                .into(binding.ivJugador)
                        }
                }

            }
        }
    }
}