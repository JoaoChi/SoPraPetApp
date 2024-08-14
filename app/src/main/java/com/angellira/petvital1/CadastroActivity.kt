package com.angellira.petvital1

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
import com.angellira.petvital1.databinding.ActivityCadastroBinding
import com.angellira.petvital1.model.Usuario
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.launch

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var preferencesManager: PreferencesManager
    private val users = UsersApi.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        preferencesManager = PreferencesManager(this)
        cadastrarUsuario()
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

        binding.background.load(R.drawable.fundo, imageLoader)
    }

    private fun cadastrarUsuario() {
        binding.BotaoRegistrar.setOnClickListener {
            val email = binding.textoregistroEmail.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmpassword = binding.password2.text.toString()
            val name = binding.usernameEditText.text.toString()
            val id = 1
            val imagem = ""

            val usuario = Usuario(id, name, email, password, imagem)
            if (password != confirmpassword) {
                Toast.makeText(this, "As senhas devem ser iguais!", Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Insira todos os dados!", Toast.LENGTH_SHORT).show()
            } else if (email.contains("@")) {
                lifecycleScope.launch {
                    val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "Petvital.db"
                    ).build()
                    val usuarioDao = db.usuarioDao()
                    usuarioDao.cadastrarUsuario(usuario)
                    startActivity(Intent(this@CadastroActivity, LoginActivity::class.java))
                }
            } else {
                Toast.makeText(this, "Adicione um email Válido!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityCadastroBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

