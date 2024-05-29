package net.pacofloridoquesada.teammanager.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Elimina ActionBar de la parte superior de la screen.
        // Siempre se tiene que poner después de sentContentView()
        supportActionBar!!.hide()
    }
}