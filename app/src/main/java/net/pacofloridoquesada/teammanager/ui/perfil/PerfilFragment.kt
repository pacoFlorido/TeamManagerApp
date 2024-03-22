package net.pacofloridoquesada.teammanager.ui.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.pacofloridoquesada.teammanager.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment() {
    private var _binding: FragmentPerfilBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPerfilBinding.inflate(inflater, container, false)

        binding.ivPerfilEditar.setOnClickListener {

        }
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}