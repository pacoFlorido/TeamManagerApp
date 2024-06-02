package net.pacofloridoquesada.teammanager.ui.main.eventos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.adapters.EventosAdapter
import net.pacofloridoquesada.teammanager.databinding.FragmentEventosBinding
import net.pacofloridoquesada.teammanager.ui.main.equipo.EquipoViewModel
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel

class EventosFragment : Fragment() {

    private var _binding: FragmentEventosBinding? = null
    private val binding get() = _binding!!
    private val equipoViewModel: EquipoViewModel by activityViewModels()
    private val teamManagerViewModel: TeamManagerViewModel by activityViewModels()
    private val eventosViewModel: EventosViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var eventosAdapter: EventosAdapter

    private fun toCrearEvento() {
        binding.fabCrearEvento.setOnClickListener {
            findNavController().navigate(EventosFragmentDirections.toCrearEvento(0))
        }
    }

    private fun setupEsEntrenador() {
        equipoViewModel.getTrainer(auth.currentUser!!.uid)

        equipoViewModel.trainer.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.fabCrearEvento.visibility = View.VISIBLE
                this.toCrearEvento()
            } else {
                binding.fabCrearEvento.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupEquipo() {
        teamManagerViewModel.getTeamByUser(auth.currentUser!!.uid)

        teamManagerViewModel.team.observe(viewLifecycleOwner) { team ->
            if (team != null) {
                eventosViewModel.getNextEventsByTeam(team.id!!)
            }
        }
    }

    private fun setupEventos() {
        eventosAdapter = EventosAdapter()

        eventosViewModel.eventos.observe(viewLifecycleOwner) { eventos ->
            if (eventos != null) {
                eventosAdapter.setLista(eventos)
            }

            with(binding.rvEventos) {
                //Creamos el layoutManager
                layoutManager = LinearLayoutManager(activity)
                //Le asignamos el adaptador
                adapter = eventosAdapter
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupEquipo()
        this.setupEventos()
        this.setupEsEntrenador()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}