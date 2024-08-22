package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

            R.id.profile_edit -> {
                startActivity(Intent(this, EditarPerfilActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
