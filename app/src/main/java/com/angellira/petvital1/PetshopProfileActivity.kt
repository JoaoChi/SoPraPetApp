package com.angellira.petvital1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import coil.load
import com.angellira.petvital1.database.AppDatabase
import com.angellira.petvital1.databinding.ActivityPetProfileBinding
import com.angellira.petvital1.databinding.ActivityPetshopProfileBinding
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PetshopProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetshopProfileBinding
    private lateinit var preferencesManager: PreferencesManager
    private var petshopId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

            setupView()
            preferencesManager = PreferencesManager(this)
            window.statusBarColor = ContextCompat.getColor(this, R.color.corfundo)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundo)
            setSupportActionBar(findViewById(R.id.barra_tarefas))
            carregandoPet()
            excluirPet()
        }

    private fun setupView() {
        binding = ActivityPetshopProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun carregandoPet() {
        val pegandoNome = intent.getStringExtra("nome_petshop")
        val pegandoPhoto = intent.getStringExtra("foto_petshop")
        val pegandoDescricao = intent.getStringExtra("descricao")
        val pegandoServicos = intent.getStringExtra("servicos")
        val pegandolocalizacao = intent.getStringExtra("localizacao")

        binding.descricaoPetshop.text = "Descrição: $pegandoDescricao"
        binding.imagePetshop.load(pegandoPhoto)
        binding.nomePetshop.text = "Nome: $pegandoNome"
        binding.servicosPetshop.text = "Serviços: $pegandoServicos"
        binding.localizacaoPetshop.text = "Localização: $pegandolocalizacao"

    }

    private fun excluirPet() {
        binding.excluirPetshop.setOnClickListener {
            lifecycleScope.launch(IO) {
                deletePetshop()
                withContext(Main) {
                    startActivity(Intent(this@PetshopProfileActivity, PetshopsActivity::class.java))
                    Toast.makeText(this@PetshopProfileActivity, "Petshop Excluido!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    private fun deletePetshop() {

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Petvital.db"
        ).build()
        val petshopDao = db.petshopDao()
        petshopId = intent.getLongExtra("uid", 0)

        petshopDao.deletarPetshop(petshopId)
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
