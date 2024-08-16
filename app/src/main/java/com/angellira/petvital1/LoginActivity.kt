package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
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
import com.angellira.petvital1.databinding.ActivityLoginBinding
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundo)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundo)

        setupView()
        preferencesManager = PreferencesManager(this)
        verificaLogin()
        botaoRegistro()
        botaoLogin()
        fundoAnimado()
        botaoEditSenha()
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

    private fun botaoLogin() {
        binding.botaoLogin.setOnClickListener {
            lifecycleScope.launch(IO) {
                verificarLogin(this@LoginActivity)
                finishAffinity()
                preferencesManager.estaLogado = true
            }
        }
    }

    private suspend fun verificarLogin(
        context: Context,
    ) {

        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "Petvital.db"
        ).build()

        val email = binding.textEmailLogin.text.toString()
        preferencesManager.userId = email
        val senha = binding.editTextPassword.text.toString()

        val usuarioDao = db.usuarioDao()
        val usuario = usuarioDao.pegarEmailUsuario(email)

        if (usuario == null) {
            withContext(Main) {
                Toast.makeText(this@LoginActivity, "Esse cadastro não existe!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (usuario.email == email && usuario.password == senha) {
            withContext(Main) {
                Toast.makeText(this@LoginActivity, "Login efetuado!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        } else {
            withContext(Main) {
                Toast.makeText(this@LoginActivity, "Email ou senha incorretos!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun botaoEditSenha(){
        binding.textEsqueciAsenha.setOnClickListener{
            startActivity(Intent(this, EsqueciASenhaActivity::class.java))
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

