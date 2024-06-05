package net.pacofloridoquesada.teammanager.ui.main.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentEditarPerfilBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentPerfilBinding
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel


class EditarPerfilFragment : Fragment() {

    private var _binding: FragmentEditarPerfilBinding?= null
    private val binding get() = _binding!!
    private val perfilViewModel: PerfilViewModel by activityViewModels()

    private fun setupDatosJugadorOEntrenador(){
        var jugador = true

        perfilViewModel.trainer.observe(viewLifecycleOwner) {
            if (it != null){
                binding.etNombreCompletoEditar.setText(it.name)

                binding.tvAliasEdicion.visibility = View.INVISIBLE
                binding.etAliasEdicion.visibility = View.INVISIBLE
                this.actualizarEntrenador()
                jugador = false
            } else {
                binding.tvAliasEdicion.visibility = View.VISIBLE
                binding.etAliasEdicion.visibility = View.VISIBLE
            }
        }
        if (jugador) {

            perfilViewModel.player.observe(viewLifecycleOwner) {
                if (it != null){
                    binding.etNombreCompletoEditar.setText(it.name)
                    binding.etAliasEdicion.setText(it.alias)
                }
            }

            this.actualizarJugador()
        }
    }

    fun setupVolver() {
        binding.btVolver.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun actualizarJugador() {
        binding.btGuardar.setOnClickListener {
            perfilViewModel.player.observe(viewLifecycleOwner) { player ->
                if (player != null) {

                    if (!binding.etNombreCompletoEditar.text.isEmpty()) {
                        player.name = binding.etNombreCompletoEditar.text.toString()
                        player.alias = binding.etAliasEdicion.text.toString()

                        perfilViewModel.updatePlayerRecord(player)

                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            this.requireContext(),
                            "El nombre es obligatorio.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
        }
    }

    private fun actualizarEntrenador() {
        binding.btGuardar.setOnClickListener {
            perfilViewModel.trainer.observe(viewLifecycleOwner) { trainer ->
                if (trainer != null) {

                    if (!binding.etNombreCompletoEditar.text.isEmpty()) {
                        trainer.name = binding.etNombreCompletoEditar.text.toString()
                        perfilViewModel.updateTrainer(trainer)
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            this.requireContext(),
                            "El nombre es obligatorio.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupDatosJugadorOEntrenador()
        this.setupVolver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarPerfilBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}