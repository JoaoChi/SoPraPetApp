package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.petvital1.databinding.ActivityMainBinding
import com.angellira.petvital1.model.User
import com.angellira.petvital1.preferences.PreferencesManager
import com.angellira.petvital1.preferences.preferenciaCadastro

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var preferencesManager: PreferencesManager

    val cadastro = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = PreferencesManager(this)
        escreverString()
        botaoConta()
        botaoCadastro()
    }

    private fun escreverString() {
        cadastro.username = preferencesManager.username ?: cadastro.username
        binding.textPet.text = "Bem vindo:\n ${cadastro.username}"
    }

    private fun botaoConta() {
        binding.botaoConta.setOnClickListener {
            startActivity(Intent(this, MinhacontaActivity::class.java))
        }
    }

    private fun botaoCadastro(){
        binding.imageAddcat.setOnClickListener{
            startActivity(Intent(this, CadastrarPet::class.java))
        }
    }
}

