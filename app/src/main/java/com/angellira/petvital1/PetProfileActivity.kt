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
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PetProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetProfileBinding
    private lateinit var preferencesManager: PreferencesManager
    private var petId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        preferencesManager = PreferencesManager(this)
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundo)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundo)
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
        val pegandoIdade = intent.getStringExtra("idade")

        binding.textDescricao.text = "Descrição: $pegandoDescricao"
        binding.imageOpen.load(pegandoPhoto)
        binding.textNome.text = "Nome: $pegandoNome"
        binding.textPeso.text = "Peso: $pegandoPeso"
        binding.textIdade.text = "Idade: $pegandoIdade"

    }

    private fun excluirPet() {
        binding.excluirPet.setOnClickListener {
            lifecycleScope.launch(IO) {
                deletePet()
                withContext(Main) {
                    startActivity(Intent(this@PetProfileActivity, MainActivity::class.java))
                    Toast.makeText(this@PetProfileActivity, "Pet Excluido!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    private fun deletePet() {

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Petvital.db"
        ).build()
        val petDao = db.petDao()
        petId = intent.getLongExtra("id", 0)

        petDao.deletarPet(petId)
    }

    private fun showPopupMenu(view: View) {

        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.inicio -> {
                    Toast.makeText(this, "Voltando ao início", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@PetProfileActivity, MainActivity::class.java))
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
                    startActivity(Intent(this@PetProfileActivity, EditarPerfilActivity::class.java))
                    true
                }
                R.id.sair -> {
                    showConfirmationDialog(
                        title = "Deseja sair?",
                        message = "Certeza que deseja deslogar?",
                        positiveAction = {
                            Toast.makeText(this, "Deslogando", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@PetProfileActivity, LoginActivity::class.java))
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
