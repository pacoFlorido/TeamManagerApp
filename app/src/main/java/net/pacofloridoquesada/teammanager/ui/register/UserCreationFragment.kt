package net.pacofloridoquesada.teammanager.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.navArgs
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentUserCreationBinding

class UserCreationFragment : Fragment() {

    private var _binding: FragmentUserCreationBinding? = null
    private val binding get() = _binding!!
    val args: UserCreationFragmentArgs by navArgs()

    private fun inicializarSpinnerPaises(){
        val adapter = ArrayAdapter.createFromResource(this.requireContext(), R.array.paises , R.layout.spinner_items)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)

        binding.spPaises.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserCreationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.inicializarSpinnerPaises()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}