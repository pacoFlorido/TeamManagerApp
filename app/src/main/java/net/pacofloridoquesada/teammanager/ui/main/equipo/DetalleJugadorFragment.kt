package net.pacofloridoquesada.teammanager.ui.main.equipo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentClasificacionBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentDetalleJugadorBinding
import net.pacofloridoquesada.teammanager.ui.main.eventos.CrearEventoFragmentArgs
import net.pacofloridoquesada.teammanager.ui.main.perfil.PerfilViewModel
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel
import java.time.LocalDate
import java.time.LocalDateTime

class DetalleJugadorFragment : Fragment() {

    private var _binding: FragmentDetalleJugadorBinding? = null
    private val binding get() = _binding!!
    private val args: DetalleJugadorFragmentArgs by navArgs()
    private val perfilViewModel: PerfilViewModel by activityViewModels()

    fun setupIniciarlizarDatosJugador() {
        perfilViewModel.getPlayer(args.userJugador)

        perfilViewModel.player.observe(viewLifecycleOwner) {
            if (it != null) {

                val anyoNacimiento = Integer.parseInt(it.birthday.substring(0,4))
                val mesNacimiento = Integer.parseInt(it.birthday.substring(5,7))
                val diaNacimiento = Integer.parseInt(it.birthday.substring(8,10))




                val hoy = LocalDateTime.now()
                var edad = hoy.year - anyoNacimiento - 1
                if ((hoy.monthValue - mesNacimiento.toLong()) > 0L) {
                    edad++
                } else if ((hoy.monthValue - mesNacimiento.toLong()) == 0L){
                    if ((hoy.dayOfMonth - diaNacimiento) > 0) {
                        edad++
                    }
                }

                binding.tvNombreUser.text = it.name
                binding.tvNacionalidad2.text = it.nationality
                binding.tvEdad2.text = edad.toString()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupIniciarlizarDatosJugador()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetalleJugadorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}