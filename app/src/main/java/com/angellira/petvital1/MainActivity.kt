package com.angellira.petvital1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.petvital1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        escreverString()
        botaoConta()
    }

    private fun escreverString() {
        val name = intent.getStringExtra("nome")
        binding.textPet.text = "Bem vindo\n " + name
    }

    private fun botaoConta() {
        binding.botaoConta.setOnClickListener {
            startActivity(Intent(this, MinhacontaActivity::class.java))
        }
    }
}

