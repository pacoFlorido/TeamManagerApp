package net.pacofloridoquesada.teammanager.ui.main.eventos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.databinding.FragmentCrearEventoBinding
import net.pacofloridoquesada.teammanager.model.Event
import net.pacofloridoquesada.teammanager.ui.register.UserCreationFragmentArgs
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CrearEventoFragment : Fragment() {

    private var _binding: FragmentCrearEventoBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val args: CrearEventoFragmentArgs by navArgs()
    private val eventosViewModel: EventosViewModel by activityViewModels()


    private fun validarCampos() {
        if (!binding.etTituloEvento.text.isEmpty() &&
            !binding.tvFechaEvento.text.isEmpty() &&
            !binding.tvHoraEvento.text.isEmpty()
        ) {
            this.guardarEvento()
        } else {
            Toast.makeText(
                this.requireContext(),
                "Los campos con '*' son obligatorios",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    private fun setupCancelar() {
        binding.btCancelar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupGuardar() {
        binding.btGuardarEvento.setOnClickListener {
            this.validarCampos()
        }
    }

    private fun guardarEvento() {
        val fecha = binding.tvFechaEvento.text.toString()
        val hora = binding.tvHoraEvento.text.toString()
        val fechaBBDD = fecha.substring(6, 10) + "-" +
                fecha.substring(3, 5) + "-" +
                fecha.substring(0, 2) + "T" + hora + ":00"


        if (args.idEvento == 0) {

            eventosViewModel.createEvent(
                Event(
                    null,
                    binding.etTituloEvento.text.toString(),
                    binding.etDescripcion.text.toString(),
                    fechaBBDD
                ),
                auth.currentUser!!.uid
            )
        } else {
        }



        findNavController().popBackStack()
    }

    private fun inicializarDatePickers() {
        val cal = Calendar.getInstance()
        val calendario = Calendar.getInstance()

        val hora = TimePickerDialog.OnTimeSetListener { timepicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            this.actualizarHora(cal)
        }

        val fecha = DatePickerDialog.OnDateSetListener { datepicker, year, month, day ->

            calendario.set(Calendar.YEAR, year)
            calendario.set(Calendar.MONTH, month)
            calendario.set(Calendar.DAY_OF_MONTH, day)

            this.actualizarFecha(calendario)
        }

        binding.btSeleccionarHora.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                hora,
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.btSelectFecha2.setOnClickListener {
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
        binding.tvFechaEvento.text = formatoSimple.format(calendar.time)
    }

    private fun actualizarHora(calendar: Calendar) {
        val formatoHora = SimpleDateFormat("HH:mm")
        binding.tvHoraEvento.text = formatoHora.format(calendar.time)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.inicializarDatePickers()
        this.setupCancelar()
        this.setupGuardar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearEventoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}