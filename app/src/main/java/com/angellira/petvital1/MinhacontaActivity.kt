package com.angellira.petvital1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.angellira.petvital1.databinding.ActivityMinhacontaBinding

class MinhacontaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMinhacontaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMinhacontaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        botaoVoltar()
        botaoPropaganda()
        botaoEditProfile()
        }

    private fun botaoVoltar() {
        binding.buttonVoltapaginainicial.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun botaoPropaganda() {
        binding.botaopetshop.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun botaoEditProfile() {
        binding.buttoneditprofile.setOnClickListener {
            startActivity(Intent(this, EditarPerfilActivity::class.java))
        }
    }
}
