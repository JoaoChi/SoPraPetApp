package com.angellira.petvital1

import android.app.AlertDialog
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
import com.angellira.petvital1.database.dao.UsuarioDao
import com.angellira.petvital1.databinding.ActivityCadastroBinding
import com.angellira.petvital1.model.User
import com.angellira.petvital1.model.Usuario
import com.angellira.petvital1.network.UsuariosApiService
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var userApi: UsuariosApiService

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

    private fun registroUsuario() {
        binding.BotaoRegistrar.setOnClickListener {
            val nome = binding.usernameEditText.text.toString()
            val email = binding.textoregistroEmail.text.toString()
            val senha = binding.passwordEditText.text.toString()
            val senha2 = binding.password2.text.toString()
            val cpf = ""
            val imagem = ""

            if (senha != senha2) {
                Toast.makeText(this, "As senhas devem coincidir! ", Toast.LENGTH_SHORT).show()
            } else if (nome.isEmpty()
                || email.isEmpty()
                || senha.isEmpty()
                || senha2.isEmpty()
            ) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    lifecycleScope.launch(IO) {
                        cadastrarUsuario(
                            this@CadastroActivity,
                            nome,
                            email,
                            senha,
                            cpf,
                            imagem
                        )
                        withContext(Main) {
                            startActivity(Intent(this@CadastroActivity, LoginActivity::class.java))
                            Toast.makeText(
                                this@CadastroActivity,
                                "Usuario Cadastrado!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
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
        val usuarioExiste = withContext(Main) {
            usuarioDao.pegarEmailUsuario(email)
        }

        if (usuarioExiste != null) {
            withContext(Main) {
                Toast.makeText(this@CadastroActivity, "Email Já existe!", Toast.LENGTH_SHORT).show()
            }
            return
        }

            val novoUsuario = Usuario(
                name = nome,
                email = email,
                password = senha,
                imagem = imagem,
                cpf = cpf
            )
            val novoUser = User(
                name = nome,
                email = email,
                password = senha,
                imagem = imagem,
                cpf = cpf
            )

            withContext(IO) {
                usuarioDao.cadastrarUsuario(novoUsuario)
                userApi.createUser(novoUser.toString())
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