package net.pacofloridoquesada.teammanager.ui.teamcreate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentCrearEquipoBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentUnirseEquipoBinding
import net.pacofloridoquesada.teammanager.ui.MainActivity
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel

class UnirseEquipoFragment : Fragment() {

    private var _binding: FragmentUnirseEquipoBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val teamManagerViewModel: TeamManagerViewModel by activityViewModels()

    private fun setupUnirseAEquipo() {
        binding.btUnirseAEquipo.setOnClickListener {
            teamManagerViewModel.getTeam(binding.etCodigoEquipo.text.toString())
            teamManagerViewModel.team.observe(viewLifecycleOwner) {
                if (it != null) {
                    teamManagerViewModel.joinTeam(
                        binding.etCodigoEquipo.text.toString(),
                        auth.currentUser!!.uid
                    )
                    this.requireActivity().finish()
                    this.requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
                } else {
                    Toast.makeText(
                        this.requireContext(),
                        "El código introducido no coincide con el de ningún equipo.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupUnirseAEquipo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUnirseEquipoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
