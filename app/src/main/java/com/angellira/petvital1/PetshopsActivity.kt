package com.angellira.petvital1

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
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.recyclerview.adapter.ListaFotos
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class PetshopsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val petshops = UsersApi.retrofitService
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setSupportActionBar(findViewById(R.id.barra_tarefas))
        mostrarPetshops()
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
        menuInflater.inflate(R.menu.petshops, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.voltarPagina -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
//            R.id.pesquisar ->{
//                startActivity(Intent(this, CadastrarPetActivity::class.java))
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun mostrarPetshops() {
        lifecycleScope.launch {
            delay(1.seconds)
            val listaPet = petshops.getPetshop().values.toList()
            Log.d("ListResult", "ListResult: ${listaPet}")
            recyclerView = binding.textItensRecyclerview
            binding.textItensRecyclerview.layoutManager =
                LinearLayoutManager(this@PetshopsActivity)
//            val adapter = ListaFotos(
//                listaPet,
//                onItemClickListener = { pet ->
//                    val intent = Intent(this@PetshopsActivity, PetProfileActivity::class.java)
//                    intent.putExtra("descricao", pet.descricao)
//                    intent.putExtra("foto_pet", pet.imagem)
//                    intent.putExtra("nome_pet", pet.name)
//                    intent.putExtra("idade", pet.idade)
//                    intent.putExtra("peso", pet.peso)
//                    startActivity(intent)
//                }
//            )
//            recyclerView.adapter = adapter
        }
    }
}
