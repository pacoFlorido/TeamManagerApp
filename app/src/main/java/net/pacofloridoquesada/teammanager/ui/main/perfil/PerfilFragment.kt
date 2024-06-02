package net.pacofloridoquesada.teammanager.ui.main.perfil

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentPerfilBinding
import net.pacofloridoquesada.teammanager.ui.login.LoginActivity
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PerfilFragment : Fragment() {
    private var _binding: FragmentPerfilBinding?= null
    private val binding get() = _binding!!
    private val perfilViewModel: PerfilViewModel by activityViewModels()
    private val teamManagerViewModel: TeamManagerViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    private fun setupDatosJugadorOEntrenador(){
        teamManagerViewModel.getTeamByUser(auth.currentUser!!.uid)

        teamManagerViewModel.team.observe(viewLifecycleOwner){
            if (it != null) {
                binding.tvEquipo.text = it.name
            }
        }
        perfilViewModel.getTrainer(auth.currentUser!!.uid)
        var jugador = true

        perfilViewModel.trainer.observe(viewLifecycleOwner) {
            if (it != null){
                val date = it.birthday
                val dateFormatted = date.substring(8, 10) + "-" +
                        date.substring(5, 7) + "-" +
                        date.substring(0,4)

                binding.tvNombreUser.text = it.name
                binding.tvEdad.text = dateFormatted
                binding.tvRolUser.text = "Entrenador"
                binding.tvNacionalidad.text = it.nationality

                binding.tvAlias.visibility = View.INVISIBLE
                binding.tvAliasDato.visibility = View.INVISIBLE
                jugador = false
            } else {
                binding.tvAlias.visibility = View.VISIBLE
                binding.tvAliasDato.visibility = View.VISIBLE
            }
        }
        if (jugador) {
            perfilViewModel.getPlayer(auth.currentUser!!.uid)
            perfilViewModel.player.observe(viewLifecycleOwner) {
                if (it != null){
                    binding.tvNombreUser.text = it.name
                    binding.tvEdad.text = it.birthday
                    binding.tvRolUser.text = "Jugador"
                    binding.tvNacionalidad.text = it.nationality
                    binding.tvAliasDato.text = it.alias
                }
            }
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
                    this.teamManagerViewModel.deletePlayerByUser(user)
                    this.teamManagerViewModel.deleteTrainerByUser(user)

                    auth.currentUser!!.delete().addOnCompleteListener {
                        if (it.isSuccessful) {

                            Log.i("Usuario Eliminado", "Usuario Eliminado")
                            this.requireActivity().finish()
                            this.requireActivity()
                                .startActivity(Intent(requireContext(), LoginActivity::class.java))
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

    private fun setUpEditar(){
        binding.ivPerfilEditar.setOnClickListener {

        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupCerrarSesion()
        this.setupEliminarUsuario()
        this.setupDatosJugadorOEntrenador()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}