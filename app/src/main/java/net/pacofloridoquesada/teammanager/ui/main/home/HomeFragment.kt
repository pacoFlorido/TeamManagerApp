package net.pacofloridoquesada.teammanager.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.databinding.FragmentHomeBinding
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    private fun setupProximoEvento() {
        homeViewModel.nextEvent.observe(viewLifecycleOwner){ event ->
            Log.i("Event: ", event.toString())
            if (event == null) {
                binding.tvNoHayEventoProximo.visibility = View.VISIBLE
            } else {
                binding.tvNoHayEventoProximo.visibility = View.INVISIBLE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupProximoEvento()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        homeViewModel.getNextEvent(auth.currentUser!!.uid)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}