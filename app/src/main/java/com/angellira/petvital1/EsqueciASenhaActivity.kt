package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.angellira.petvital1.database.AppDatabase
import com.angellira.petvital1.databinding.ActivityCadastroBinding
import com.angellira.petvital1.databinding.ActivityEsqueciAsenhaBinding
import com.angellira.petvital1.model.Usuario
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EsqueciASenhaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEsqueciAsenhaBinding
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundo)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundo)
        preferencesManager = PreferencesManager(this)
        editSenha()
    }

    private fun editSenha() {
        binding.botaoRedefinirEVoltar.setOnClickListener {
            lifecycleScope.launch(IO) {
                trocarSenha(this@EsqueciASenhaActivity)
                withContext(Main){
                    Toast.makeText(this@EsqueciASenhaActivity, "Senha Atualizada!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@EsqueciASenhaActivity, LoginActivity::class.java))
                }
            }
        }
    }

    private suspend fun trocarSenha(
        context: Context,
    ) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "Petvital.db"
        ).build()

        val email = binding.editEmail.text.toString()
        val novasenha1 = binding.editTextNumberPassword.text.toString()
        val novasenha2 = binding.editTextNumberPassword2.text.toString()

        val usuarioDao = db.usuarioDao()
        usuarioDao.pegarEmailUsuario(email)

        if (novasenha2.isEmpty() || novasenha1.isEmpty() || email.isEmpty()) {
            withContext(Main) {
                Toast.makeText(this@EsqueciASenhaActivity, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }else if(novasenha2 != novasenha1){
            withContext(Main){
                Toast.makeText(this@EsqueciASenhaActivity, "As senhas devem ser iguais!", Toast.LENGTH_SHORT).show()
            }
        }else{
            usuarioDao.updateSenha(novasenha1, email)
        }

    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityEsqueciAsenhaBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}