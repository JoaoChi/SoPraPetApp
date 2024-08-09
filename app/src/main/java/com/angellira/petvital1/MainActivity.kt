package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angellira.petvital1.databinding.ActivityCadastroBinding
import com.angellira.petvital1.databinding.ActivityMainBinding
import com.angellira.petvital1.model.User
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import com.angellira.petvital1.preferences.preferenciaCadastro
import com.angellira.petvital1.recyclerview.adapter.ListaFotos
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferencesManager: PreferencesManager
    private val pets = UsersApi.retrofitService
    private lateinit var recyclerView: RecyclerView

    val cadastro = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setupView()
        setSupportActionBar(findViewById(R.id.barra_tarefas))
        preferencesManager = PreferencesManager(this)
        mandandoImagens()
        escreverString()
    }

    private fun mandandoImagens() {
        lifecycleScope.launch {
                delay(1.seconds)
                val listaPet = pets.getPets().values.toList()
                Log.d("ListResult", "ListResult: ${listaPet}")
                recyclerView = binding.textItensRecyclerview
                binding.textItensRecyclerview.layoutManager =
                    LinearLayoutManager(this@MainActivity)
                val adapter = ListaFotos(
                    listaPet,
                    onItemClickListener = { descricao, imagem, nome, idade, peso ->
                        val intent = Intent(this@MainActivity, PetProfileActivity::class.java)
                        intent.putExtra("descricao", descricao)
                        intent.putExtra("foto_pet", imagem)
                        intent.putExtra("nome_pet", nome)
                        intent.putExtra("idade", idade)
                        intent.putExtra("peso", peso)
                        startActivity(intent)
                    }
                )
            recyclerView.adapter = adapter
        }
    }

    private fun escreverString() {
        cadastro.username = preferencesManager.username ?: cadastro.username
        binding.textPet.text = "Bem vindo ${cadastro.username}!"
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
                startActivity(Intent(this, CadastrarPet::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

