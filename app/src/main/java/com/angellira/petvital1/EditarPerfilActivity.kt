package com.angellira.petvital1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundociano)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundociano)
        setSupportActionBar(findViewById(R.id.barra_tarefas))
        botaoExcluirConta()
        botaoEsqueciaSenha()
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

            val userDao = db.usuarioDao()
            if (email != null) {
                userDao.deletarUsuario(email)
            }else{
                withContext(Main){
                    Toast.makeText(this@EditarPerfilActivity, "Email não localizado.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.editprofile, menu)
        return true
    }

    private fun botaoEsqueciaSenha() {
        binding.button2esquecisenha.setOnClickListener {
            startActivity(Intent(this, EsqueciASenhaActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.voltarPagina -> {
                startActivity(Intent(this, MinhacontaActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}