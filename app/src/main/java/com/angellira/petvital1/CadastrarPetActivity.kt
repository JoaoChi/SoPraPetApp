package com.angellira.petvital1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.petvital1.databinding.ActivityCadastrarPetBinding
import com.angellira.petvital1.model.Pet
import com.angellira.petvital1.network.UsersApi
import kotlinx.coroutines.launch

class CadastrarPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarPetBinding
    private val pets = UsersApi.retrofitService


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
        cadastrarPet()
    }

    private fun cadastrarPet() {
        binding.buttonSalvarPet.setOnClickListener {

            val nome = binding.editNomePet.text.toString()
            val description = binding.editRacaPet.text.toString()
            val peso = binding.editPeso.text.toString()
            val idade = binding.editIdade.text.toString()
            val imagem = binding.editImagem.text.toString()
            val id = ""
            val context = this
            val pet = Pet(id, nome, description, peso, idade, imagem)

            if (nome.isNotEmpty() &&
                description.isNotEmpty() &&
                peso.isNotEmpty() &&
                idade.isNotEmpty() &&
                imagem.isNotEmpty()
            ) {
                lifecycleScope.launch {
                    pets.savePets(pet)
                    startActivity(Intent(this@CadastrarPetActivity, MainActivity::class.java))
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

