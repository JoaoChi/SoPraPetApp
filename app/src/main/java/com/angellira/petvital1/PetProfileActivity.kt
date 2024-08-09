package com.angellira.petvital1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.angellira.petvital1.databinding.ActivityMainBinding
import com.angellira.petvital1.databinding.ActivityPetProfileBinding

class PetProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setupView()
        setSupportActionBar(findViewById(R.id.barra_tarefas))
        carregandoPet()
        }


    private fun setupView() {
        binding = ActivityPetProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun carregandoPet() {
        val pegandoNome = intent.getStringExtra("nome_pet")
        val pegandoPhoto = intent.getStringExtra("foto_pet")
        val pegandoDescricao = intent.getStringExtra("descricao")
        val pegandoPeso = intent.getStringExtra("peso")
        val pegandoIdade =  intent.getStringExtra("idade")

        binding.textDescricao.text = "Descrição: $pegandoDescricao"
        binding.imageOpen.load(pegandoPhoto)
        binding.textNome.text = "Nome: $pegandoNome"
        binding.textPeso.text = "Peso: $pegandoPeso"
        binding.textIdade.text = "Idade: $pegandoIdade"

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.editprofile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.voltarPagina -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
