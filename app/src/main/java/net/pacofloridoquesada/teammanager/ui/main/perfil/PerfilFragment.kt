package net.pacofloridoquesada.teammanager.ui.main.perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.databinding.FragmentPerfilBinding
import net.pacofloridoquesada.teammanager.ui.login.LoginActivity

class PerfilFragment : Fragment() {
    private var _binding: FragmentPerfilBinding?= null
    private val binding get() = _binding!!

    private fun setupCerrarSesion(){
        binding.btCerrarSesion.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            requireActivity().finish()
            requireActivity().startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupCerrarSesion();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}