package com.angellira.petvital1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import com.angellira.petvital1.model.User
import com.angellira.petvital1.model.Usuario
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var preferencesManager: PreferencesManager
    private val PICK_IMAGE_REQUEST = 1
    private var imagemBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        preferencesManager = PreferencesManager(this)
        registroUsuario()
        fundoAnimado()
        pegarImagem()
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

    private fun pegarImagem() {
        binding.botaoAddfoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
    super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST
            && resultCode == Activity.RESULT_OK
            && data != null){
            val imageUri = data.data

            imagemBase64 = encodeImageToBase64(imageUri!!)
        }
    }

    fun encodeImageToBase64(imageUri: Uri): String? {
        val imageStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(imageStream)

        if (bitmap == null) {
            return null
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()

        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
    }

    private fun registroUsuario() {
        binding.BotaoRegistrar.setOnClickListener {
            val nome = binding.usernameEditText.text.toString()
            val email = binding.textoregistroEmail.text.toString()
            val senha = binding.passwordEditText.text.toString()
            val senha2 = binding.password2.text.toString()
            val cpf = "123124"

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
                    lifecycleScope.launch {
                        cadastrarUsuario(
                            this@CadastroActivity,
                            nome,
                            email,
                            senha,
                            cpf,
                            imagemBase64 ?: ""
                        )
                            startActivity(Intent(this@CadastroActivity, LoginActivity::class.java))
                            Toast.makeText(
                                this@CadastroActivity,
                                "Usuario Cadastrado!",
                                Toast.LENGTH_SHORT
                            ).show()
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
        cpf: String,
        imagem: String
    ) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "Petvital.db"
        ).build()

        val usuarioDao = db.usuarioDao()
        val usuarioExiste = withContext(IO) {
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

        val userApi = UsersApi.retrofitService
        withContext(IO) {
                usuarioDao.cadastrarUsuario(novoUsuario)
            userApi.saveUser(novoUser)
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