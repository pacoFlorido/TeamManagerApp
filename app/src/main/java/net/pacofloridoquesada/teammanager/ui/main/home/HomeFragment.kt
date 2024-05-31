package net.pacofloridoquesada.teammanager.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    private fun setupProximoEvento() {
        homeViewModel.nextEvent.observe(viewLifecycleOwner){ event ->
            Log.i("Event: ", event.toString())
            if (event == null) {
                binding.tvNoHayEventoProximo.visibility = View.VISIBLE
            } else {
                binding.tvNoHayEventoProximo.visibility = View.INVISIBLE
                binding.tvEventoTitulo.text = event.titulo
                binding.tvEventoDesc.text = event.descripcion
                val fecha = event.fecha
                val mesNumero = fecha.substring(5,7)
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
                val dia = fecha.substring(8,10)
                val hora = fecha.substring(11,16)

                binding.tvEventoDia.text = dia
                binding.tvEventoMes.text = mes
                binding.tvEventoHora.text = hora
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupProximoEvento()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        homeViewModel.getNextEvent(auth.currentUser!!.uid)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}