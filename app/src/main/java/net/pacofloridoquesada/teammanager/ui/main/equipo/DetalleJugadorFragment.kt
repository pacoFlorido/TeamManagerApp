package net.pacofloridoquesada.teammanager.ui.main.equipo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage
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
    private lateinit var auth: FirebaseAuth
    private val perfilViewModel: PerfilViewModel by activityViewModels()

    fun mostrarEditar() {
        perfilViewModel.getTrainer(auth.currentUser!!.uid)

        perfilViewModel.trainer.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivEditarJugador.visibility = View.VISIBLE
            } else {
                binding.ivEditarJugador.visibility = View.INVISIBLE
            }
        }
    }

    fun toEditarEstadisticas() {
        binding.ivEditarJugador.setOnClickListener {
            val action = DetalleJugadorFragmentDirections.toActualizarJugador(args.userJugador)
            findNavController().navigate(action)
        }
    }

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
                binding.tvGolesEstats.text = it.playerReport!!.goals.toString()
                binding.tvAsistenciasEstats.text = it.playerReport.assists.toString()
                binding.tvPartidosEstats.text = it.playerReport.matches.toString()
                binding.tvAmarillasEstats.text = it.playerReport.yellowCards.toString()
                binding.tvRojasEstats.text = it.playerReport.redCards.toString()
                if (it.image != null) {
                    Firebase.storage.reference.child("image").child(it.image!!)
                        .downloadUrl.addOnSuccessListener {uri ->
                            Glide.with(binding.cvPerfil.context)
                                .load(uri)
                                .error(R.drawable.ic_logo_app)
                                .into(binding.ivPerfil)
                        }
                }
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
        this.mostrarEditar()
        this.toEditarEstadisticas()
        this.setupIniciarlizarDatosJugador()
        this.setupVolver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetalleJugadorBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}