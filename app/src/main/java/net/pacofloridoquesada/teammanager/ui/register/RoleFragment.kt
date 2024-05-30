package net.pacofloridoquesada.teammanager.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import net.pacofloridoquesada.teammanager.databinding.FragmentRoleBinding

class RoleFragment : Fragment() {

    private var _binding: FragmentRoleBinding? = null
    private val binding get() = _binding!!

    private fun setupSiguiente() {
        binding.btnSiguiente.setOnClickListener {
            if (binding.rbJugador.isChecked) {
                findNavController()
                    .navigate(
                        RoleFragmentDirections.toCrearUser(1)
                    )
            } else {
                findNavController()
                    .navigate(
                        RoleFragmentDirections.toCrearUser(2)
                    )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setupSiguiente()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}