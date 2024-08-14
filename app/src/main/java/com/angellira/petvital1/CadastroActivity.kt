package com.angellira.petvital1

import android.content.Context
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        registroUsuario()
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

    private fun registroUsuario(){
        binding.BotaoRegistrar.setOnClickListener {
            val nome = binding.usernameEditText.text.toString()
            val email = binding.textoregistroEmail.text.toString()
            val senha = binding.passwordEditText.text.toString()
            val senha2 = binding.password2.text.toString()
            val cpf = ""
            val imagem = ""

            if (senha != senha2) {
                Toast.makeText(this, "As senhas devem coincidir! ", Toast.LENGTH_SHORT).show()
            } else if (nome.isNullOrEmpty()
                || email.isNullOrEmpty()
                || senha.isNullOrEmpty()
                || senha2.isNullOrEmpty()
            ) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    try {
                        cadastrarUsuario(
                            this@CadastroActivity,
                            nome,
                            email,
                            senha,
                            cpf,
                            imagem
                        )
                        Toast.makeText(
                            this@CadastroActivity,
                            "Usuario Cadastrado!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@CadastroActivity,
                            "Falha no cadastro.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
    }

    private suspend fun cadastrarUsuario(
        context: Context,
        nome: String,
        email: String,
        senha: String,
        imagem: String,
        cpf: String
    ) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "Petvital.db"
        ).build()

        val usuarioDao = db.usuarioDao()

        val usuarioExiste = withContext(Dispatchers.IO){
            usuarioDao.pegarEmailUsuario(email)
        }

        if(usuarioExiste != null){
            Toast.makeText(this, "Email Já existe!", Toast.LENGTH_SHORT).show()
        }

        val novoUsuario = Usuario(
            name = nome,
            email = email,
            password = senha,
            imagem = imagem,
            cpf = cpf
        )
        return withContext(Dispatchers.IO){
            usuarioDao.cadastrarUsuario(novoUsuario)
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

