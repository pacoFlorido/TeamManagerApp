package net.pacofloridoquesada.teammanager.ui.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentUserCreationBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class UserCreationFragment : Fragment() {

    private var _binding: FragmentUserCreationBinding? = null
    private val binding get() = _binding!!
    val args: UserCreationFragmentArgs by navArgs()
    private lateinit var auth : FirebaseAuth

    private fun iniciarCreacionUsuario(){
        binding.btCrearUser.setOnClickListener{
            validarCampos()
        }
    }

    private fun validarCampos(){
        var message: String
        if (!binding.etNombreCompleto.text.isEmpty() &&
            !binding.tvFechaNacimiento.text.isEmpty() &&
            !binding.etEmailCreacionUsu.text.isEmpty() &&
            !binding.etContrasenyaCrear.text.isEmpty() &&
            !binding.etRepetirContrasenya.text.isEmpty()){
            this.crearUsuarioFirebase()
        } else {
            Toast.makeText(
                this.requireContext(),
                "Debes rellenar todos los campos para crear tu nuevo usuario.",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    private fun inicializarDatePicker(){
        val calendario = Calendar.getInstance()
        val fecha = DatePickerDialog.OnDateSetListener{datepicker, year, month, day ->
            calendario.set(Calendar.YEAR, year)
            calendario.set(Calendar.MONTH, month)
            calendario.set(Calendar.DAY_OF_MONTH, day)

            this.actualizarFecha(calendario)
        }

        binding.btSelectFecha.setOnClickListener{
            DatePickerDialog(
                requireContext(),
                fecha,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun actualizarFecha (calendar: Calendar){
        val formatoFecha = "dd-MM-yyyy"
        val formatoSimple = SimpleDateFormat(formatoFecha, Locale.ENGLISH)
        binding.tvFechaNacimiento.text = formatoSimple.format(calendar.time)
    }

    private fun crearUsuarioFirebase() {
        val email = binding.etEmailCreacionUsu.text.toString()
        val contrasenya = binding.etContrasenyaCrear.text.toString()

        auth.createUserWithEmailAndPassword(email, contrasenya).addOnCompleteListener{
            if (it.isSuccessful){
                if (args.roleSelected == 1){
                    //Creamos Jugador

                } else {
                    //Creamos Entrenador

                }
            }
        }
    }

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

        this.iniciarCreacionUsuario()
        this.inicializarDatePicker()
        this.inicializarSpinnerPaises()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}