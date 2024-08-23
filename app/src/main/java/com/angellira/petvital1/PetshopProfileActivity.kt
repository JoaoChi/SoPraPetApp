package com.angellira.petvital1

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
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
import androidx.room.Room
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
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
            window.statusBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundo)
            setSupportActionBar(findViewById(R.id.barra_tarefas))
            carregandoPet()
            excluirPet()
            fundoAnimado()
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

        binding.background.load(R.drawable.fundoamarelo, imageLoader)
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

    private fun showPopupMenu(view: View) {

        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.inicio -> {
                    Toast.makeText(this, "Voltando ao início", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@PetshopProfileActivity, MainActivity::class.java))
                    true
                }
                R.id.perfil -> {
                    Toast.makeText(this, "Seu perfil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MinhacontaActivity::class.java))
                    true
                }
                R.id.ajuda -> {
                    Toast.makeText(this, "Sem página ainda", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.config -> {
                    Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@PetshopProfileActivity, EditarPerfilActivity::class.java))
                    true
                }
                R.id.sair -> {
                    showConfirmationDialog(
                        title = "Deseja sair?",
                        message = "Certeza que deseja deslogar?",
                        positiveAction = {
                            Toast.makeText(this, "Deslogando", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@PetshopProfileActivity, LoginActivity::class.java))
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
        menuInflater.inflate(R.menu.editprofile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.voltarPagina -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }

            R.id.configs -> {
                showPopupMenu(findViewById(R.id.configs))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
