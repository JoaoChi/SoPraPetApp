package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angellira.petvital1.databinding.ActivityMainBinding
import com.angellira.petvital1.model.User
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import com.angellira.petvital1.recyclerview.adapter.ListaFotos
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferencesManager: PreferencesManager
    private val pets = UsersApi.retrofitService
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferencia = getSharedPreferences(
            "USER_PREFERENCES", Context.MODE_PRIVATE
        )

        setupView()
        setSupportActionBar(findViewById(R.id.barra_tarefas))
        preferencesManager = PreferencesManager(this)
        mandandoImagens(preferencia)
    }

    private fun mandandoImagens(preferencia: SharedPreferences) {
        lifecycleScope.launch {
                delay(1.seconds)
                val listaPet = pets.getPets().values.toList()
                Log.d("ListResult", "ListResult: ${listaPet}")
                recyclerView = binding.textItensRecyclerview
                binding.textItensRecyclerview.layoutManager =
                    LinearLayoutManager(this@MainActivity)
                val adapter = ListaFotos(
                    listaPet,
                    onItemClickListener = { pet ->
                        criarId(preferencia)
                        val intent = Intent(this@MainActivity, PetProfileActivity::class.java)
                        intent.putExtra("descricao", pet.descricao)
                        intent.putExtra("foto_pet", pet.imagem)
                        intent.putExtra("nome_pet", pet.name)
                        intent.putExtra("idade", pet.idade)
                        intent.putExtra("peso", pet.peso)
                        startActivity(intent)
                    }
                )
            recyclerView.adapter = adapter
        }
    }

    private fun criarId(preferencia: SharedPreferences) {
        val petId = gerarIdPet()
        preferencesManager.petId = petId
        preferencia.edit().putString("Idpet", petId).apply()
    }

    private fun gerarIdPet(length: Int = 8): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.itens, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile_action -> {
                startActivity(Intent(this, MinhacontaActivity::class.java))
                true
            }
            R.id.action_favorite ->{
                startActivity(Intent(this, CadastrarPetActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

