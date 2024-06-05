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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
    private lateinit var storageRef: StorageReference

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
                val fechaFormateada = date.substring(8,10) + "-" +
                        date.substring(5,7) + "-" +
                        date.substring(0,4)

                binding.tvNombreUser.text = it.name
                binding.tvEdad.text = fechaFormateada
                binding.tvPerfilTitle.text = "Perfil Entrenador"
                binding.tvNacionalidad.text = it.nationality
                if (it.image != null) {
                    val imageRef = storageRef.child("image").child(it.image!!)

                    Glide.with(binding.cvPerfil.context)
                        .load(imageRef)
                        .error(R.drawable.ic_logo_app)
                        .into(binding.ivPerfil)
                }


                binding.tvAliasDato.visibility = View.INVISIBLE
                jugador = false
            } else {
                binding.tvAliasDato.visibility = View.VISIBLE
            }
        }
        if (jugador) {

            perfilViewModel.getPlayer(auth.currentUser!!.uid)
            perfilViewModel.player.observe(viewLifecycleOwner) {
                if (it != null){
                    val date = it.birthday
                    val fechaFormateada = date.substring(8,10) + "-" +
                            date.substring(5,7) + "-" +
                            date.substring(0,4)
                    binding.tvNombreUser.text = it.name
                    binding.tvEdad.text = fechaFormateada
                    binding.tvPerfilTitle.text = "Perfil Jugador"
                    binding.tvNacionalidad.text = it.nationality
                    binding.tvAliasDato.text = it.alias
                    if (it.image != null) {
                        val imageRef = storageRef.child("image").child(it.image!!)

                        Glide.with(binding.cvPerfil.context)
                            .load(imageRef)
                            .error(R.drawable.ic_logo_app)
                            .into(binding.ivPerfil)
                    }
                }
            }
        }

        binding.tvEmail.text = auth.currentUser!!.email
    }

    private fun toOpcionesApp() {
        binding.ivOpciones.setOnClickListener {
            val action = PerfilFragmentDirections.toOpciones()
            findNavController().navigate(action)
        }
    }



    private fun toEditar(){
        binding.ivPerfilEditar.setOnClickListener {
            findNavController().navigate(PerfilFragmentDirections.toEditarPerfil())
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupDatosJugadorOEntrenador()
        this.toEditar()
        this.toOpcionesApp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}