package net.pacofloridoquesada.teammanager.ui.register

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentUserCreationBinding
import net.pacofloridoquesada.teammanager.model.Player
import net.pacofloridoquesada.teammanager.model.Trainer
import net.pacofloridoquesada.teammanager.ui.teamcreate.TeamCreateActivity
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class UserCreationFragment : Fragment() {

    private var _binding: FragmentUserCreationBinding? = null
    private val binding get() = _binding!!
    private val args: UserCreationFragmentArgs by navArgs()
    private lateinit var auth: FirebaseAuth
    val teamManagerViewModel: TeamManagerViewModel by activityViewModels()

    private fun iniciarCreacionUsuario() {
        binding.btCrearUser.setOnClickListener {
            validarCampos()
        }
    }

    private fun validarCampos() {

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = binding.tvFechaNacimiento.text.toString()
        val localDateFechaNacimiento = LocalDate.parse(date, formatter)


        if (!binding.etNombreCompleto.text.isEmpty() &&
            !binding.tvFechaNacimiento.text.isEmpty() &&
            !binding.etEmailCreacionUsu.text.isEmpty() &&
            !binding.etContrasenyaCrear.text.isEmpty() &&
            !binding.etRepetirContrasenya.text.isEmpty()
        ) {
            if (binding.etContrasenyaCrear.text.toString().equals(binding.etRepetirContrasenya.text.toString())){
                if (!localDateFechaNacimiento.isAfter(LocalDate.now())){
                    this.crearUsuarioFirebase()
                } else {
                    Toast.makeText(
                        this.requireContext(),
                        "La fecha de nacimiento no puede ser mayor a la de hoy",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            } else {
                Toast.makeText(
                    this.requireContext(),
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        } else {
            Toast.makeText(
                this.requireContext(),
                "Debes completar todos los campos",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    private fun inicializarDatePicker() {
        val calendario = Calendar.getInstance()

        val fecha = DatePickerDialog.OnDateSetListener { datepicker, year, month, day ->

            calendario.set(Calendar.YEAR, year)
            calendario.set(Calendar.MONTH, month)
            calendario.set(Calendar.DAY_OF_MONTH, day)

            this.actualizarFecha(calendario)
        }

        binding.btSelectFecha.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                fecha,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun actualizarFecha(calendar: Calendar) {
        val formatoFecha = "dd-MM-yyyy"
        val formatoSimple = SimpleDateFormat(formatoFecha, Locale.ENGLISH)
        binding.tvFechaNacimiento.text = formatoSimple.format(calendar.time)
    }

    private fun crearUsuarioFirebase() {
        val email = binding.etEmailCreacionUsu.text.toString()
        val contrasenya = binding.etContrasenyaCrear.text.toString()

        auth.createUserWithEmailAndPassword(email, contrasenya).addOnCompleteListener {
            if (it.isSuccessful) {
                if (args.roleSelected == 1) {
                    val fechaNormal = binding.tvFechaNacimiento.text.toString()
                    val fechaBBDD = fechaNormal.substring(6, 10) + "-" +
                            fechaNormal.substring(3, 5) + "-" +
                            fechaNormal.substring(0, 2)
                    //Creamos Jugador
                    teamManagerViewModel.addPlayer(
                        Player(
                            null,
                            "",
                            fechaBBDD,
                            binding.etNombreCompleto.text.toString(),
                            binding.spPaises.selectedItem.toString(),
                            null,
                            auth.currentUser?.uid.toString()
                        )
                    )
                    this.requireActivity().finish()
                    this.requireActivity().startActivity(Intent(requireContext(), TeamCreateActivity::class.java))
                } else {
                    val fechaNormal = binding.tvFechaNacimiento.text.toString()
                    val fechaBBDD = fechaNormal.substring(6, 10) + "-" +
                            fechaNormal.substring(3, 5) + "-" +
                            fechaNormal.substring(0, 2)
                    //Creamos Entrenador
                    teamManagerViewModel.addTrainer(
                        Trainer(
                            fechaBBDD,
                            null,
                            binding.etNombreCompleto.text.toString(),
                            binding.spPaises.selectedItem.toString(),
                            auth.currentUser?.uid.toString()
                        )
                    )
                    this.requireActivity().finish()
                    this.requireActivity().startActivity(Intent(requireContext(), TeamCreateActivity::class.java))
                }
            } else {
                val errorMessage: String
                if (it.exception!!.message!!.contains("formatted")){
                    errorMessage = "El formato del email es incorrecto"
                } else if (it.exception!!.message!!.contains("email")){
                    errorMessage = "El email utilizado ya está en uso"
                } else {
                    errorMessage = "La contraseña debe estar compuesta de al menos 6 carácteres"
                }
                Toast.makeText(
                    this.requireContext(),
                    errorMessage,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun inicializarSpinnerPaises() {
        val adapter = ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.paises,
            R.layout.spinner_items
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)

        binding.spPaises.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserCreationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.iniciarCreacionUsuario()
        this.inicializarDatePicker()
        this.inicializarSpinnerPaises()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}