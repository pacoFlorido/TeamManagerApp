package net.pacofloridoquesada.teammanager.ui.main.equipo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentActualizarJugadorBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentDetalleJugadorBinding
import net.pacofloridoquesada.teammanager.ui.main.perfil.PerfilViewModel

class ActualizarJugadorFragment : Fragment() {

    private var _binding: FragmentActualizarJugadorBinding? = null
    private val binding get() = _binding!!
    private val args: DetalleJugadorFragmentArgs by navArgs()
    private val perfilViewModel: PerfilViewModel by activityViewModels()

    private fun setupGuardar() {
        binding.btActualizarJugador.setOnClickListener {
            this.validarCampos()
        }
    }

    private fun validarCampos() {
        if (!binding.etGoles.text.isEmpty() &&
            !binding.etAsistencias.text.isEmpty() &&
            !binding.etPartidos.text.isEmpty() &&
            !binding.etAmarillas.text.isEmpty() &&
            !binding.etRojas.text.isEmpty()
        ) {
            this.actualizarJugador()
        } else {
            Toast.makeText(
                this.requireContext(),
                "No se debe dejar ningún campo vacío, completa con '0' si no hay datos para ese campo.",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    private fun cargarDatos() {
        perfilViewModel.getPlayer(args.userJugador)

        perfilViewModel.player.observe(viewLifecycleOwner) { player ->
            if (player != null) {
                binding.tvNombreUser.text = player.name

                binding.etGoles.setText(player.playerReport!!.goals.toString())
                binding.etAsistencias.setText(player.playerReport.assists.toString())
                binding.etPartidos.setText(player.playerReport.matches.toString())
                binding.etAmarillas.setText(player.playerReport.yellowCards.toString())
                binding.etRojas.setText(player.playerReport.redCards.toString())

                if (player.image != null) {
                    Firebase.storage.reference.child("image").child(player.image!!)
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

    private fun actualizarJugador() {
        perfilViewModel.player.observe(viewLifecycleOwner) { player ->
            if (player != null) {

                player.playerReport!!.goals = binding.etGoles.text.toString().toInt()
                player.playerReport.assists = binding.etAsistencias.text.toString().toInt()
                player.playerReport.matches = binding.etPartidos.text.toString().toInt()
                player.playerReport.yellowCards = binding.etAmarillas.text.toString().toInt()
                player.playerReport.redCards = binding.etRojas.text.toString().toInt()

                perfilViewModel.updatePlayerRecord(player)

                findNavController().popBackStack()
            }
        }
    }

    private fun setupCancelar() {
        binding.btCancelarActualizacion.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.cargarDatos()
        this.setupGuardar()
        this.setupCancelar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActualizarJugadorBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}