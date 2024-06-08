package net.pacofloridoquesada.teammanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.ItemEntrenadorBinding
import net.pacofloridoquesada.teammanager.databinding.ItemJugadorBinding
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.model.Trainer

class EntrenadoresAdapter : RecyclerView.Adapter<EntrenadoresAdapter.EntrenadoresViewHolder>() {

    var listaTrainer: List<Trainer>? = null
    var onEntrenadorClickListener: OnEntrenadorClickListener? = null

    fun setLista(lista: List<Trainer>) {
        listaTrainer = lista
        notifyDataSetChanged()
    }

    inner class EntrenadoresViewHolder(val binding: ItemEntrenadorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val entrenador = listaTrainer?.get(this.adapterPosition)
                onEntrenadorClickListener?.onEntrenadorClick(entrenador)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntrenadoresViewHolder {
        val binding =
            ItemEntrenadorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntrenadoresViewHolder(binding)
    }

    override fun getItemCount(): Int = listaTrainer?.size ?: 0

    override fun onBindViewHolder(holder: EntrenadoresViewHolder, position: Int) {
        with(holder) {
            with(listaTrainer!!.get(position)) {
                binding.tvNombreJugador.text = name
                if (image != null) {
                    Firebase.storage.reference.child("image").child(image!!)
                        .downloadUrl.addOnSuccessListener { uri ->
                            Glide.with(binding.cvEntrenador.context)
                                .load(uri)
                                .error(R.drawable.ic_logo_app)
                                .into(binding.ivJugador)
                        }

                }
            }
        }
    }

    interface OnEntrenadorClickListener {
        fun onEntrenadorClick(entrenador: Trainer?)
    }
}