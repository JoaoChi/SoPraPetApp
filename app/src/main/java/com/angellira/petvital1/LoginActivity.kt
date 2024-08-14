package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.angellira.petvital1.databinding.ActivityLoginBinding
import com.angellira.petvital1.model.User
import com.angellira.petvital1.model.Usuario
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val users = UsersApi.retrofitService
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundo)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundo)

        val preferencia = getSharedPreferences(
            "USER_PREFERENCES", Context.MODE_PRIVATE
        )

        setupView()
        preferencesManager = PreferencesManager(this)
        verificaLogin()
        botaoRegistro()
        botaoLogin(preferencia)
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
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun botaoLogin(preferencia: SharedPreferences) {
        binding.botaoLogin.setOnClickListener {
            val email = binding.textEmailLogin.text.toString()
            val senha = binding.editTextPassword.text.toString()
            val context = this

            lifecycleScope.launch {
                val validacao = verificarLogin(email, senha, preferencia)
                if (validacao) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finishAffinity()
                    preferencesManager.estaLogado = true
                } else {
                    preferencesManager.estaLogado = false
                    Toast.makeText(context, "Email ou senha incorretos!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun verificarLogin(
        email: String,
        senha: String,
        preferencia: SharedPreferences
    ): Boolean {
        return if (email.isNotEmpty() && senha.isNotEmpty()) {
            val usuarios = users.getUsers()
            preferencesManager.userId = usuarios.entries.find { it.value.email == email }?.key
            preferencia.edit().putString("Id", preferencesManager.userId).apply()
            usuarios.values.any { it.email == email && it.password == senha }
        } else {
            false
        }
    }


    private fun botaoRegistro() {
        binding.BotaoRegistrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }

    private fun verificaLogin() {
        val estaLogado = preferencesManager.estaLogado
        if (estaLogado) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

