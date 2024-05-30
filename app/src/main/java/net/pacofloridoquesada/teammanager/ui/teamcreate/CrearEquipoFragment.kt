package net.pacofloridoquesada.teammanager.ui.teamcreate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentCrearEquipoBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentSelectCrearUnirBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentUserCreationBinding
import net.pacofloridoquesada.teammanager.model.Team
import net.pacofloridoquesada.teammanager.ui.MainActivity
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel
import java.time.LocalDate

class CrearEquipoFragment : Fragment() {

    private var _binding: FragmentCrearEquipoBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val teamManagerViewModel: TeamManagerViewModel by activityViewModels()

    private fun setupCrearEquipo() {
        binding.btCrearEquipoNuevo.setOnClickListener {
            if (!binding.etNombreEquipo.text.isEmpty()) {
                this.teamManagerViewModel.addTeam(
                    Team(
                        binding.etCiudadEquipo.text.toString(),
                        LocalDate.now().toString(),
                        null,
                        binding.etNombreEquipo.text.toString()
                    ),
                    auth.currentUser!!.uid
                )

                this.requireActivity().finish()
                this.requireActivity().startActivity(Intent(requireContext(),MainActivity::class.java))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setupCrearEquipo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearEquipoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}