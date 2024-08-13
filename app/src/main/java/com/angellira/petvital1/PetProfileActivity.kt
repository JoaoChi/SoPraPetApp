package com.angellira.petvital1

import android.app.AlertDialog
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.angellira.petvital1.databinding.ActivityMainBinding
import com.angellira.petvital1.databinding.ActivityPetProfileBinding
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.launch

class PetProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetProfileBinding
    private val pets = UsersApi.retrofitService
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        preferencesManager = PreferencesManager(this)

        setupView()
        setSupportActionBar(findViewById(R.id.barra_tarefas))
        fundoAnimado()
        carregandoPet()
        excluirPet()
        }

    private fun fundoAnimado() {
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        binding.background.load(R.drawable.fundo, imageLoader)
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

    private fun excluirPet(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage("Certeza que deseja apagar seu pet?")
            .setTitle("Excluir Pet!")
            .setPositiveButton("Sim") { dialog, wich ->
                try {
                        deletePet()
                        startActivity(Intent(this, MainActivity::class.java))
                        Toast.makeText(this, "Seu pet foi excluido!", Toast.LENGTH_SHORT).show()
                        preferencesManager.logout()
                        finishAffinity()
                } catch (e: Exception) {
                    Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
                }
            }
        val dialog: AlertDialog = builder.create()
        binding.excluirPet.setOnClickListener {
            dialog.show()
        }
    }

    private fun deletePet(){
        lifecycleScope.launch {
            val id = preferencesManager.petId
            pets.deletePet(id.toString())
        }
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
