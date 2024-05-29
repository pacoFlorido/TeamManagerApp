package net.pacofloridoquesada.teammanager.ui.teamcreate

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentSelectCrearUnirBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentUserCreationBinding
import net.pacofloridoquesada.teammanager.ui.login.LoginActivity

class SelectCrearUnirFragment : Fragment() {

    private var _binding: FragmentSelectCrearUnirBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private fun setupCerrarSesion() {
        binding.btCerrarSesionPantallaEquipo.setOnClickListener{
            auth.signOut()
            this.requireActivity().finish()
            this.requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }

    private fun setupEliminarUsuario() {
        binding.btEliminarCuentaPantallaEquipo.setOnClickListener{
            AlertDialog.Builder(activity as Context)
                .setTitle(R.string.eliminacion_de_cuenta)
                .setMessage(R.string.eliminacion_cuenta_mensaje)
                // Acción si pulsa si
                .setPositiveButton(getString(R.string.si)){ v, _->
                    auth.currentUser!!.delete().addOnCompleteListener {
                        if (it.isSuccessful){
                            Log.i("Usuario Eliminado" , "Usuario Eliminado")
                            this.requireActivity().finish()
                            this.requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
                        }
                    }
                    //Cerramos el dialogo
                    v.dismiss()
                }
                // Accion si pulsa no
                .setNegativeButton(getString(R.string.no)){ v, _->v.dismiss()}
                .setCancelable(false)
                .create()
                .show()

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setupCerrarSesion()
        this.setupEliminarUsuario()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectCrearUnirBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}