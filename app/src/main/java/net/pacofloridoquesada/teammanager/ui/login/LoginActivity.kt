package net.pacofloridoquesada.teammanager.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import net.pacofloridoquesada.teammanager.databinding.ActivityLoginBinding
import net.pacofloridoquesada.teammanager.ui.MainActivity
import net.pacofloridoquesada.teammanager.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null){
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Oculto la barra de ayuda ya que no la voy a utilizar
        supportActionBar!!.hide()
        setupIniciarSesion()
        setupRegistro()
    }

    private fun setupIniciarSesion(){
        binding.btnIniciarSesion.setOnClickListener{

            val email = binding.etEmail.text
            val contrasenya = binding.etContrasenya.text
            if (email.isNotEmpty() && contrasenya.isNotEmpty()) {
                auth.signInWithEmailAndPassword(
                    email.toString(),
                    contrasenya.toString()
                ).addOnCompleteListener{
                    if (it.isSuccessful){
                        finish()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // Si el inicio de sesión falla, muestro un mensaje indicando el error.
                        Log.w(TAG, "createUserWithEmail:failure", it.exception)
                        Toast.makeText(
                            baseContext,
                            "Error iniciando sesión.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    baseContext,
                    "Introduce un Email y una Contraseña.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun setupRegistro(){
        binding.btnRegistrarse.setOnClickListener{

            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}