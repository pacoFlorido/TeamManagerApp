package net.pacofloridoquesada.teammanager.ui.teamcreate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.ActivityMainBinding
import net.pacofloridoquesada.teammanager.databinding.ActivityTeamCreateBinding
import net.pacofloridoquesada.teammanager.ui.MainActivity
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel

class TeamCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Elimina ActionBar de la parte superior de la screen.
        // Siempre se tiene que poner después de sentContentView()
        supportActionBar!!.hide()
    }
}