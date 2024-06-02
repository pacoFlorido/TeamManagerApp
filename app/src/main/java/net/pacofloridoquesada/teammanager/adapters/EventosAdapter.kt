package net.pacofloridoquesada.teammanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.pacofloridoquesada.teammanager.databinding.ItemEventoBinding
import net.pacofloridoquesada.teammanager.model.Event

class EventosAdapter : RecyclerView.Adapter<EventosAdapter.EventosViewHolder>() {

    var listaEventos: List<Event>? = null

    fun setLista(lista: List<Event>) {
        listaEventos = lista
        notifyDataSetChanged()
    }

    inner class EventosViewHolder(val binding: ItemEventoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventosViewHolder {
        val binding = ItemEventoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventosViewHolder(binding)
    }

    override fun getItemCount(): Int = listaEventos?.size ?: 0

    override fun onBindViewHolder(holder: EventosViewHolder, position: Int) {
        with(holder){
            with(listaEventos!!.get(position)) {
                binding.tvEventoTitulo.text = titulo
                binding.tvEventoDesc.text = descripcion
                val fecha = fecha
                val mesNumero = fecha.substring(5, 7)
                var mes = ""
                when (mesNumero) {
                    "01" -> mes = "Ene"
                    "02" -> mes = "Feb"
                    "03" -> mes = "Mar"
                    "04" -> mes = "Abr"
                    "05" -> mes = "May"
                    "06" -> mes = "Jun"
                    "07" -> mes = "Jul"
                    "08" -> mes = "Ago"
                    "09" -> mes = "Sep"
                    "10" -> mes = "Oct"
                    "11" -> mes = "Nov"
                    "12" -> mes = "Dic"
                }
                val dia = fecha.substring(8, 10)
                val hora = fecha.substring(11, 16)

                binding.tvEventoDia.text = dia
                binding.tvEventoMes.text = mes
                binding.tvEventoHora.text = hora
            }
        }
    }
}