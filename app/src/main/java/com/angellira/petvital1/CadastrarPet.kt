package com.angellira.petvital1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.petvital1.databinding.ActivityCadastrarPetBinding

class CadastrarPet : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarPetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCadastrarPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        botaoRegistrar()
    }

    private fun botaoRegistrar() {
        binding.buttonSalvarPet.setOnClickListener {
        startActivity(Intent(this, MainActivity::class.java))
        }
    }
}

