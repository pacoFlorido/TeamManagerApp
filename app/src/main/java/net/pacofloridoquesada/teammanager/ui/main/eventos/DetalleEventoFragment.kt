package net.pacofloridoquesada.teammanager.ui.main.eventos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import net.pacofloridoquesada.teammanager.databinding.FragmentDetalleEventoBinding

class DetalleEventoFragment : Fragment() {

    private var _binding: FragmentDetalleEventoBinding? = null
    private val binding get() = _binding!!
    private val args: CrearEventoFragmentArgs by navArgs()
    private val eventosViewModel: EventosViewModel by activityViewModels()

    private fun mostrarDatos() {
        eventosViewModel.getEventById(args.idEvento)

        eventosViewModel.evento.observe(viewLifecycleOwner) {
            if (it != null) {
                val fechaCompleta = it.fecha
                val fechaFormateada = fechaCompleta.substring(8,10) + "-" +
                        fechaCompleta.substring(5,7) + "-" +
                        fechaCompleta.substring(0,4)
                val horaFormateada = fechaCompleta.substring(11,13) + ":" + fechaCompleta.substring(14,16)

                binding.tvTituloEvento.text = it.titulo
                binding.tvFechaEventoVisualizar.text = fechaFormateada
                binding.tvHoraEvento.text = horaFormateada
                binding.tvDatoDescripcion.text = it.descripcion
            }
        }
    }

    private fun setupVolver() {
        binding.btVolver.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mostrarDatos()
        this.setupVolver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetalleEventoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}