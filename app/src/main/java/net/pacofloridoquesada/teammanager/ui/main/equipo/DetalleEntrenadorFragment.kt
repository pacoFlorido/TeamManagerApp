package net.pacofloridoquesada.teammanager.ui.main.equipo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentActualizarJugadorBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentDetalleEntrenadorBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentDetalleEquipoBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentEquipoBinding
import java.time.LocalDateTime

class DetalleEntrenadorFragment : Fragment() {

    private var _binding: FragmentDetalleEntrenadorBinding? = null
    private val binding get() = _binding!!
    private val args: DetalleEntrenadorFragmentArgs by navArgs()
    private val equipoViewModel: EquipoViewModel by activityViewModels()

    private fun setupEntrenador() {
        equipoViewModel.getTrainer(args.userTrainer)

        equipoViewModel.trainer.observe(viewLifecycleOwner) {
            if (it != null) {

                val anyoNacimiento = Integer.parseInt(it.birthday.substring(0,4))
                val mesNacimiento = Integer.parseInt(it.birthday.substring(5,7))
                val diaNacimiento = Integer.parseInt(it.birthday.substring(8,10))

                val hoy = LocalDateTime.now()
                var edad = hoy.year - anyoNacimiento - 1
                if ((hoy.monthValue - mesNacimiento.toLong()) > 0L) {
                    edad++
                } else if ((hoy.monthValue - mesNacimiento.toLong()) == 0L){
                    if ((hoy.dayOfMonth - diaNacimiento) > 0) {
                        edad++
                    }
                }

                binding.tvNombreEntrenador.text = it.name
                binding.tvNacionalidadEntrenador.text = it.nationality
                binding.tvEdadEntrenador.text = edad.toString()
                if (it.image != null) {
                    Firebase.storage.reference.child("image").child(it.image!!)
                        .downloadUrl.addOnSuccessListener {uri ->
                            Glide.with(binding.cvPerfil.context)
                                .load(uri)
                                .error(R.drawable.ic_logo_app)
                                .into(binding.ivPerfil)
                        }
                }
            }
        }
    }

    private fun setupVolver() {
        binding.btVolver.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupEntrenador()
        this.setupVolver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetalleEntrenadorBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}