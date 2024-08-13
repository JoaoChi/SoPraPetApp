package com.angellira.petvital1

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
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
import coil.ImageLoader
import coil.load
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.angellira.petvital1.databinding.ActivityPetshopsBinding
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import com.angellira.petvital1.recyclerview.adapter.ListaPetshops
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class PetshopsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetshopsBinding
    private val petshops = UsersApi.retrofitService
    private lateinit var recyclerView: RecyclerView
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setSupportActionBar(findViewById(R.id.barra_petshops))
        preferencesManager = PreferencesManager(this)
        mostrarPetshops()
        botaoPropaganda()
    }

    private fun botaoPropaganda(){
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        binding.bannerPromo.load(R.drawable.mereceomelhor , imageLoader)
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityPetshopsBinding.inflate(layoutInflater)

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
            val listaPetshop = petshops.getPetshop().values.toList()
            Log.d("ListResult", "ListResult: ${listaPetshop}")
            recyclerView = binding.recyclerViewFeed
            binding.recyclerViewFeed.layoutManager =
                LinearLayoutManager(this@PetshopsActivity)
            val adapter = ListaPetshops(
                listaPetshop,
                onItemClickListener = { petshop ->
                    val intent = Intent(this@PetshopsActivity, PetProfileActivity::class.java)
                    intent.putExtra("descricao", petshop.descricao)
                    intent.putExtra("foto_petshop", petshop.imagem)
                    intent.putExtra("nome_petshop", petshop.name)
                    intent.putExtra("localizacao", petshop.localizacao)
                    intent.putExtra("servicos", petshop.servicos)
                    startActivity(intent)
                }
            )
            recyclerView.adapter = adapter
        }
    }
}
