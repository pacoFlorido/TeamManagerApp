package net.pacofloridoquesada.teammanager.ui.main.perfil

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentEditarPerfilBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentOpcionesBinding
import net.pacofloridoquesada.teammanager.ui.login.LoginActivity
import net.pacofloridoquesada.teammanager.ui.teamcreate.TeamCreateActivity
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel

class OpcionesFragment : Fragment() {

    private var _binding: FragmentOpcionesBinding?= null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val perfilViewModel: PerfilViewModel by activityViewModels()
    private val teamManagerViewModel: TeamManagerViewModel by activityViewModels()

    private fun setupVolver() {
        binding.btVolver2.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupAbandonarEquipo() {
        binding.btAbandonarEquipo.setOnClickListener {
            perfilViewModel.delteUserFromTeam(auth.currentUser!!.uid)
            this.requireActivity().finish()
            this.requireActivity().startActivity(Intent(requireContext(), TeamCreateActivity::class.java))
        }
    }

    private fun setupCerrarSesion(){
        binding.btCerrarSesionPantallaEquipo.setOnClickListener{
            auth.signOut()
            this.requireActivity().finish()
            this.requireActivity().startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
    }

    private fun setupEliminarUsuario() {
        binding.btEliminarCuentaPantallaEquipo.setOnClickListener {
            AlertDialog.Builder(activity as Context)
                .setTitle(R.string.eliminacion_de_cuenta)
                .setMessage(R.string.eliminacion_cuenta_mensaje)
                // Acción si pulsa si
                .setPositiveButton(getString(R.string.si)) { v, _ ->
                    //UID del usuario a eliminar
                    val user = auth.currentUser!!.uid

                    auth.currentUser!!.delete().addOnCompleteListener {
                        if (it.isSuccessful) {

                            this.teamManagerViewModel.deletePlayerByUser(user)
                            this.teamManagerViewModel.deleteTrainerByUser(user)
                            Log.i("Usuario Eliminado", "Usuario Eliminado")
                            this.requireActivity().finish()
                            this.requireActivity()
                                .startActivity(Intent(requireContext(), LoginActivity::class.java))
                        } else {

                        }
                    }
                    v.dismiss()
                }
                // Accion si pulsa no
                .setNegativeButton(getString(R.string.no)) { v, _ -> v.dismiss() }
                .setCancelable(false)
                .create()
                .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupEliminarUsuario()
        this.setupCerrarSesion()
        this.setupAbandonarEquipo()
        this.setupVolver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpcionesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}