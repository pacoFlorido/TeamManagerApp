package net.pacofloridoquesada.teammanager.ui.main.equipo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentActualizarJugadorBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentDetalleEntrenadorBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentDetalleEquipoBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentEquipoBinding

class DetalleEntrenadorFragment : Fragment() {

    private var _binding: FragmentDetalleEntrenadorBinding? = null
    private val binding get() = _binding!!
    private val args: DetalleJugadorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetalleEntrenadorBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
}