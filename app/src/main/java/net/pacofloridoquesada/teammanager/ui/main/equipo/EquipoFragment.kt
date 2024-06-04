package net.pacofloridoquesada.teammanager.ui.main.equipo

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.adapters.EntrenadoresAdapter
import net.pacofloridoquesada.teammanager.adapters.EventosAdapter
import net.pacofloridoquesada.teammanager.adapters.JugadoresEquipoAdapter
import net.pacofloridoquesada.teammanager.adapters.OnJugadorClickListener
import net.pacofloridoquesada.teammanager.databinding.FragmentEquipoBinding
import net.pacofloridoquesada.teammanager.model.Event
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.model.Team
import net.pacofloridoquesada.teammanager.ui.main.eventos.EventosFragmentDirections
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel

class EquipoFragment : Fragment() {

    private var _binding: FragmentEquipoBinding? = null
    private val binding get() = _binding!!
    private val equipoViewModel: EquipoViewModel by activityViewModels()
    private val teamManagerViewModel: TeamManagerViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var jugadoresAdapter: JugadoresEquipoAdapter
    private lateinit var entrenadoresAdapter: EntrenadoresAdapter

    private fun setupMostrarCodigoEquipo() {
        binding.btInvitar.setOnClickListener {
            teamManagerViewModel.team.observe(viewLifecycleOwner) {
                if (it != null) {
                    AlertDialog.Builder(activity as Context)
                        .setTitle(getString(R.string.invitarrr))
                        .setMessage("¡Envia este código a un amigo para que " +
                                "pueda unirse a tu equipo!" +
                                "\n\nCódigo del equipo: ${it.code}")
                        .setPositiveButton(android.R.string.ok) { v, _ ->
                            v.dismiss()
                        }
                        .setCancelable(false)
                        .create()
                        .show()
                }
            }

        }
    }

    private fun toUpdateOVerJugador(){
        jugadoresAdapter.onJugadorClickListener = object : OnJugadorClickListener{
            override fun onJugadorClick(jugador: Player?) {
                val action = EquipoFragmentDirections.toDetalleJugador(jugador!!.user)
                findNavController().navigate(action)
            }
        }
    }

    private fun toDetalleEquipo() {
        binding.cvEquipoBar.setOnClickListener {
            val action = EquipoFragmentDirections.toDetalleEquipo()
            findNavController().navigate(action)
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
                binding.tvCantidadJugadores.text = jugadoresAdapter.itemCount.toString()
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
                binding.tvCantidadEntrenadores.text = entrenadoresAdapter.itemCount.toString()
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
        this.setupMostrarCodigoEquipo()
        this.toUpdateOVerJugador()
        this.toDetalleEquipo()
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