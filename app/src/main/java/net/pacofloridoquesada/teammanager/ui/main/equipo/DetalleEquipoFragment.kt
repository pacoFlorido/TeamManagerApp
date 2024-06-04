package net.pacofloridoquesada.teammanager.ui.main.equipo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentActualizarJugadorBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentDetalleEquipoBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentEquipoBinding
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel

class DetalleEquipoFragment : Fragment() {

    private var _binding: FragmentDetalleEquipoBinding? = null
    private val binding get() = _binding!!
    private val equipoViewModel: EquipoViewModel by activityViewModels()
    private val teamManagerViewModel: TeamManagerViewModel by activityViewModels()

    private fun setupEquipo() {
        teamManagerViewModel.team.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvCiudad.text = it.city
                binding.tvNombreEquipo.text = it.name
            }
        }
    }

    private fun setupEquipoEstats() {
        equipoViewModel.jugadoresEquipo.observe(viewLifecycleOwner) {
            if (it != null) {
                var goles = 0
                var assistencias = 0
                var amarillas = 0
                var rojas = 0
                for (player in it) {
                    goles += player.playerReport!!.goals
                    assistencias += player.playerReport.assists
                    amarillas += player.playerReport.yellowCards
                    rojas += player.playerReport.redCards
                }

                binding.tvGolesEstats.text = goles.toString()
                binding.tvAsistenciasEstats.text = assistencias.toString()
                binding.tvAmarillasEstats.text = amarillas.toString()
                binding.tvRojasEstats.text = rojas.toString()
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
        this.setupEquipo()
        this.setupEquipoEstats()
        this.setupVolver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetalleEquipoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}