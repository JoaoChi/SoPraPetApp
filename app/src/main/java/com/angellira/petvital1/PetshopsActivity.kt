package com.angellira.petvital1

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
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
import coil.load
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.angellira.petvital1.database.AppDatabase
import com.angellira.petvital1.databinding.ActivityPetshopsBinding
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import com.angellira.petvital1.recyclerview.adapter.ListaFotos
import com.angellira.petvital1.recyclerview.adapter.ListaPetshops
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class PetshopsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetshopsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundo)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundo)
        setSupportActionBar(findViewById(R.id.barra_petshops))
        preferencesManager = PreferencesManager(this)
        mostrarPetshops()
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

    private fun showPopupMenu(view: View) {

        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.inicio -> {
                    Toast.makeText(this, "Voltando ao início", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@PetshopsActivity, MainActivity::class.java))
                    true
                }

                R.id.perfil -> {
                    Toast.makeText(this, "Seu perfil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MinhacontaActivity::class.java))
                    true
                }

                R.id.ajuda -> {
                    Toast.makeText(this, "Já está nesta página.", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.config -> {
                    Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@PetshopsActivity, EditarPerfilActivity::class.java))
                    true
                }

                R.id.sair -> {
                    showConfirmationDialog(
                        title = "Deseja sair?",
                        message = "Certeza que deseja deslogar?",
                        positiveAction = {
                            Toast.makeText(this, "Deslogando", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@PetshopsActivity, LoginActivity::class.java))
                            preferencesManager.estaLogado = false
                            finishAffinity()
                        }
                    )
                    true
                }

                R.id.privacidade -> {
                    Toast.makeText(this, "Sem página ainda", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showConfirmationDialog(title: String, message: String, positiveAction: () -> Unit) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton("Sim") { dialog, _ ->
            positiveAction.invoke()
            dialog.dismiss()
        }

        builder.setNegativeButton("Não") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
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

            R.id.pesquisar -> {
                startActivity(Intent(this, CadastrarPetshopActivity::class.java))
                true
            }

            R.id.configs -> {
                showPopupMenu(findViewById(R.id.configs))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun mostrarPetshops() {
        lifecycleScope.launch(IO) {
            try {
                val userApi = UsersApi.retrofitService
                val petshops = userApi.getPetshop()

                withContext(Main) {
                    recyclerView = binding.recyclerViewFeed
                    binding.recyclerViewFeed.layoutManager =
                        LinearLayoutManager(this@PetshopsActivity)
                    val adapter = ListaPetshops(
                        petshops,
                        onItemClickListener = { petshop ->
                            val intent =
                                Intent(this@PetshopsActivity, PetshopProfileActivity::class.java)
                            intent.putExtra("descricao", petshop.descricao)
                            intent.putExtra("foto_petshop", petshop.imagem)
                            intent.putExtra("nome_petshop", petshop.name)
                            intent.putExtra("servicos", petshop.servicos)
                            intent.putExtra("localizacao", petshop.localizacao)
                            intent.putExtra("uid", petshop.uid)
                            startActivity(intent)
                        }
                    )
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "Petvital.db"
                ).build()
                val petshopDao = db.petshopDao()
                val listaPetshop = petshopDao.getAllpetshop()
                withContext(Main) {
                    recyclerView = binding.recyclerViewFeed
                    binding.recyclerViewFeed.layoutManager =
                        LinearLayoutManager(this@PetshopsActivity)
                    val adapter = ListaPetshops(
                        listaPetshop,
                        onItemClickListener = { petshop ->
                            val intent =
                                Intent(this@PetshopsActivity, PetshopProfileActivity::class.java)
                            intent.putExtra("descricao", petshop.descricao)
                            intent.putExtra("foto_petshop", petshop.imagem)
                            intent.putExtra("nome_petshop", petshop.name)
                            intent.putExtra("servicos", petshop.servicos)
                            intent.putExtra("localizacao", petshop.localizacao)
                            intent.putExtra("uid", petshop.uid)
                            startActivity(intent)
                        }
                    )
                    recyclerView.adapter = adapter
                }
            }
        }
    }
}
