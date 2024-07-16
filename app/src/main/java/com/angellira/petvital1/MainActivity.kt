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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val cadastro = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        escreverString()
        botaoConta()
    }

    private fun escreverString() {
        val sharedPreferences = getSharedPreferences("cadastro", Context.MODE_PRIVATE)
        cadastro.username = sharedPreferences.getString("nome", cadastro.username).toString()
        val textPet2 = binding.textPet
        textPet2.text = "Bem vindo:\n ${cadastro.username}"
    }


    private fun botaoConta() {
        binding.botaoConta.setOnClickListener {
            startActivity(Intent(this, MinhacontaActivity::class.java))
        }
    }
}

