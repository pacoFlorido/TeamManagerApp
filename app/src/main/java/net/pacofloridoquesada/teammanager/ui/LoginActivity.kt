package net.pacofloridoquesada.teammanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.pacofloridoquesada.teammanager.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
    }
}