package net.pacofloridoquesada.teammanager.ui.main.clasificacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.adapters.JugadoresClasificacionAdapter
import net.pacofloridoquesada.teammanager.databinding.FragmentClasificacionBinding
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel

class ClasificacionFragment : Fragment() {

    private var _binding: FragmentClasificacionBinding? = null
    private val binding get() = _binding!!
    private val clasificacionViewModel: ClasificacionViewModel by activityViewModels()
    private val teamManagerViewModel: TeamManagerViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var jugadoresAdapter: JugadoresClasificacionAdapter

    fun setupJugadoresEquipo() {
        teamManagerViewModel.getTeamByUser(auth.currentUser!!.uid)

        teamManagerViewModel.team.observe(viewLifecycleOwner) {
            if (it != null) {
                clasificacionViewModel.getPlayersByIdTeam(it.id!!)
                binding.rgClasificacionFiltro.setOnCheckedChangeListener {_, checkedId->
                    when (checkedId) {
                        R.id.rbGoles -> clasificacionViewModel.getPlayersByIdTeamOrderBy(it.id,"goles")
                        R.id.rbAsistencias -> clasificacionViewModel.getPlayersByIdTeamOrderBy(it.id,"asistencias")
                        R.id.rbPartidos -> clasificacionViewModel.getPlayersByIdTeamOrderBy(it.id,"partidos")
                        R.id.rbAmarillas -> clasificacionViewModel.getPlayersByIdTeamOrderBy(it.id,"amarillas")
                        else -> clasificacionViewModel.getPlayersByIdTeamOrderBy(it.id,"rojas")
                    }
                }
            }
        }
    }

    fun mostrarJugadores() {
        jugadoresAdapter = JugadoresClasificacionAdapter()

        clasificacionViewModel.jugadoresEquipo.observe(viewLifecycleOwner) { jugadores ->
            if (jugadores != null) {
                jugadoresAdapter.setListaOrdenada(jugadores)
            }

            with(binding.rvClasificacion) {
                //Creamos el layoutManager
                layoutManager = LinearLayoutManager(activity)
                //Le asignamos el adaptador
                adapter = jugadoresAdapter
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupJugadoresEquipo()
        this.mostrarJugadores()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClasificacionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}