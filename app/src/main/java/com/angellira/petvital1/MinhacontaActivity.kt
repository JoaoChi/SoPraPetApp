package com.angellira.petvital1

import EditProfileDialogFragment
import android.content.Context
import android.content.Intent
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
import coil.load
import com.angellira.petvital1.database.AppDatabase
import com.angellira.petvital1.databinding.ActivityMinhacontaBinding
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MinhacontaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMinhacontaBinding
    private lateinit var preferencesManager: PreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        setSupportActionBar(findViewById(R.id.barra_tarefas))
        preferencesManager = PreferencesManager(this)
        botaoDeslogarPreferences()
        lifecycleScope.launch {
            pegarDadosUser(this@MinhacontaActivity)
        }
        botaoeditconta()
        val botaoeditarConta = binding.editProfile
        botaoeditarConta.setOnClickListener{
            val dialogFragment = EditProfileDialogFragment()
            dialogFragment.show(supportFragmentManager, "EditProfileDialogFragment")
        }
    }

    private fun setupView() {
        Intent(this, MinhacontaActivity::class.java)
        binding = ActivityMinhacontaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private suspend fun pegarDadosUser(context: Context) {
        val email = preferencesManager.userId
        lifecycleScope.launch(IO) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "Petvital.db"
            ).build()

            val usuarioDao = db.usuarioDao()
            val usuario = usuarioDao.pegarEmailUsuario(email.toString())
            withContext(Main) {
                binding.textNome.text = usuario!!.name
                binding.textCpf.text = usuario!!.cpf
                binding.textTelefone.text = usuario!!.password
                binding.imageOpen.load(usuario.imagem)

            }

        }
    }

    private fun botaoeditconta(){
        binding.editProfile.setOnClickListener{
            startActivity(Intent(this, EditarPerfilActivity::class.java))
        }
    }

    private fun botaoDeslogarPreferences() {

        val buttonDeslogar = binding.buttonsair
        buttonDeslogar.setOnClickListener {
            preferencesManager.logout()
            val deslogarLogin = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(deslogarLogin)
            finishAffinity()
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
                    startActivity(Intent(this@MinhacontaActivity, MainActivity::class.java))
                    true
                }
                R.id.perfil -> {
                    Toast.makeText(this, "Já está no perfil", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.ajuda -> {
                    Toast.makeText(this, "Sem página ainda", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.config -> {
                    Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MinhacontaActivity, EditarPerfilActivity::class.java))
                    true
                }
                R.id.sair -> {
                    showConfirmationDialog(
                        title = "Deseja sair?",
                        message = "Certeza que deseja deslogar?",
                        positiveAction = {
                            Toast.makeText(this, "Deslogando", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@MinhacontaActivity, LoginActivity::class.java))
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
        menuInflater.inflate(R.menu.profile, menu)
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
