package com.angellira.petvital1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.angellira.petvital1.database.AppDatabase
import com.angellira.petvital1.databinding.ActivityEditarPerfilBinding
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPerfilBinding
    private val users = UsersApi.retrofitService
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        preferencesManager = PreferencesManager(this)
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        setSupportActionBar(findViewById(R.id.barra_tarefas))
        botaoExcluirConta()
        conferirCheck()
    }

    private fun setupView() {
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun botaoExcluirConta() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage("Certeza que deseja excluir sua conta? você não poderá recuperá-la depois!")
            .setTitle("*Excluir Conta!*")
            .setPositiveButton("Sim") { dialog, wich ->
                try {
                    deleteUser()
                    startActivity(Intent(this, LoginActivity::class.java))
                    Toast.makeText(this, "Sua conta foi deletada!", Toast.LENGTH_SHORT).show()
                    preferencesManager.logout()
                    finishAffinity()
                } catch (e: Exception) {
                    Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
                }
            }
        val dialog: AlertDialog = builder.create()
        binding.buttonExcluirConta.setOnClickListener {
            dialog.show()
        }
    }


    private fun deleteUser() {
        lifecycleScope.launch(IO) {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "Petvital.db"
            ).build()

            val email = preferencesManager.userId
            val userApi = UsersApi.retrofitService

            val userDao = db.usuarioDao()
            if (email != null) {
                userApi.deleteUser(email)
                userDao.deletarUsuario(email)
            } else {
                withContext(Main) {
                    Toast.makeText(
                        this@EditarPerfilActivity,
                        "Email não localizado.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun conferirCheck() {
        binding.checkBox1.setOnClickListener {
            verificarCheckBoxes()
        }

        binding.checkBox2.setOnClickListener {
            verificarCheckBoxes()
        }
    }

    private fun verificarCheckBoxes() {
        if (binding.checkBox1.isChecked && binding.checkBox2.isChecked) {
            binding.buttonExcluirConta.visibility = VISIBLE
        } else {
            binding.buttonExcluirConta.visibility = GONE
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
                    startActivity(Intent(this@EditarPerfilActivity, MainActivity::class.java))
                    true
                }

                R.id.perfil -> {
                    Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@EditarPerfilActivity, MinhacontaActivity::class.java))
                    true
                }

                R.id.ajuda -> {
                    Toast.makeText(this, "Sem página ainda", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.config -> {
                    Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            this@EditarPerfilActivity,
                            EditarPerfilActivity::class.java
                        )
                    )
                    true
                }

                R.id.sair -> {
                    showConfirmationDialog(
                        title = "Deseja sair?",
                        message = "Certeza que deseja deslogar?",
                        positiveAction = {
                            Toast.makeText(this, "Deslogando", Toast.LENGTH_SHORT).show()
                            startActivity(
                                Intent(
                                    this@EditarPerfilActivity,
                                    LoginActivity::class.java
                                )
                            )
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
                startActivity(Intent(this, MinhacontaActivity::class.java))
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