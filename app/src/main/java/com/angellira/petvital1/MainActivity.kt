package com.angellira.petvital1

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.angellira.petvital1.database.AppDatabase
import com.angellira.petvital1.databinding.ActivityMainBinding
import com.angellira.petvital1.preferences.PreferencesManager
import com.angellira.petvital1.recyclerview.adapter.ListaFotos
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundociano)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundociano)

        setSupportActionBar(findViewById(R.id.barra_tarefas))
        preferencesManager = PreferencesManager(this)
        mandandoImagens()
        botaoPropaganda()
    }

    private fun botaoPropaganda() {
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        binding.bannerPromo.load(R.drawable.ver_ofertas, imageLoader)

        binding.bannerPromo.setOnClickListener {
            startActivity(Intent(this, PetshopsActivity::class.java))
        }
    }

    private fun mandandoImagens() {
        lifecycleScope.launch(IO) {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "Petvital.db"
            ).build()
            val petDao = db.petDao()
            val listaPet = petDao.getPet()

            withContext(Main){
            recyclerView = binding.textItensRecyclerview
            binding.textItensRecyclerview.layoutManager =
                LinearLayoutManager(this@MainActivity)
            val adapter = ListaFotos(
                listaPet,
                onItemClickListener = { pet ->
                    val intent = Intent(this@MainActivity, PetProfileActivity::class.java)
                    intent.putExtra("descricao", pet.descricao)
                    intent.putExtra("foto_pet", pet.imagem)
                    intent.putExtra("nome_pet", pet.name)
                    intent.putExtra("idade", pet.idade)
                    intent.putExtra("peso", pet.peso)
                    intent.putExtra("id", pet.id)
                    startActivity(intent)
                }
            )
            recyclerView.adapter = adapter
        }
        }
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

            R.id.action_favorite -> {
                startActivity(Intent(this, CadastrarPetActivity::class.java))
                true
            }

            R.id.logo -> {
                startActivity(Intent(this, PetshopsActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

