package net.pacofloridoquesada.teammanager.ui.main.equipo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.adapters.EntrenadoresAdapter
import net.pacofloridoquesada.teammanager.adapters.JugadoresEquipoAdapter
import net.pacofloridoquesada.teammanager.databinding.FragmentEquipoBinding
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel

class EquipoFragment : Fragment() {

    private var _binding: FragmentEquipoBinding? = null
    private val binding get() = _binding!!
    private val equipoViewModel: EquipoViewModel by activityViewModels()
    private val teamManagerViewModel: TeamManagerViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    lateinit var jugadoresAdapter: JugadoresEquipoAdapter
    lateinit var entrenadoresAdapter: EntrenadoresAdapter

    private fun setupUsuarioEntrenador(){
        equipoViewModel.getTrainer(auth.currentUser!!.uid)

        equipoViewModel.trainer.observe(viewLifecycleOwner){
            if (it != null){
                binding.ivAdministrarEquipo.visibility = View.VISIBLE
            } else {
                binding.ivAdministrarEquipo.visibility = View.INVISIBLE
            }
        }
    }


    private fun setupEquipo() {
        teamManagerViewModel.getTeamByUser(auth.currentUser!!.uid)

        teamManagerViewModel.team.observe(viewLifecycleOwner) { team ->
            if (team != null) {
                binding.tvNombreEquipo.text = team.name
                equipoViewModel.getPlayersByIdTeam(team.id!!)
                equipoViewModel.getTrainersByIdTeam(team.id)
            }
        }
    }

    private fun setupJugadoresEquipo() {
        jugadoresAdapter = JugadoresEquipoAdapter()

        equipoViewModel.jugadoresEquipo.observe(viewLifecycleOwner) { jugadores ->
            if (jugadores != null) {
                jugadoresAdapter.setLista(jugadores)
            }

            with(binding.rvJugadoresEquipo) {
                //Creamos el layoutManager
                layoutManager = LinearLayoutManager(activity)
                //Le asignamos el adaptador
                adapter = jugadoresAdapter
            }
        }
    }

    private fun setupEntrenadoresEquipo() {
        entrenadoresAdapter = EntrenadoresAdapter()

        equipoViewModel.entrenadoresEquipo.observe(viewLifecycleOwner) { entrenadores ->
            if (entrenadores != null) {
                entrenadoresAdapter.setLista(entrenadores)
            }

            with(binding.rvEntrenadores) {
                //Creamos el layoutManager
                layoutManager = LinearLayoutManager(activity)
                //Le asignamos el adaptador
                adapter = entrenadoresAdapter
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupEquipo()
        this.setupJugadoresEquipo()
        this.setupEntrenadoresEquipo()
        this.setupUsuarioEntrenador()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}