package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.angellira.petvital1.database.AppDatabase
import com.angellira.petvital1.databinding.ActivityCadastroBinding
import com.angellira.petvital1.model.User
import com.angellira.petvital1.model.Usuario
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import com.angellira.petvital1.preferences.preferenciaCadastro
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var preferencesManager: PreferencesManager
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        preferencesManager = PreferencesManager(this)
        registroUsuario()
        pegarImagem()
        pegarImagemUsuario()
    }

    private fun pegarImagem() {
        binding.botaoAddfoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    private fun pegarImagemUsuario(){
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val imageUri = uri

                    uploadImageToFirebase(imageUri)
                    Toast.makeText(this@CadastroActivity, "Foto selecionada", Toast.LENGTH_SHORT).show()
                    binding.textoAviso.visibility = VISIBLE
                } else {
                    Toast.makeText(this@CadastroActivity, "Nenhuma foto selecionada", Toast.LENGTH_SHORT).show()
                    binding.BotaoRegistrar.visibility = VISIBLE
                }
            }
        binding.botaoAddfoto.setOnClickListener {
            binding.BotaoRegistrar.visibility = INVISIBLE
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?) {
        if (imageUri != null) {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val emailImg = binding.textoregistroEmail.text.toString()

            val imagesRef = storageRef.child("images/$emailImg/${imageUri.lastPathSegment}")

            val uploadTask = imagesRef.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->

                    val downloadUrl = uri.toString()
                    preferencesManager.userImage = downloadUrl

                    Toast.makeText(this, "Upload bem-sucedido", Toast.LENGTH_SHORT).show()
                    binding.BotaoRegistrar.visibility = VISIBLE
                    binding.textoAviso.visibility = INVISIBLE

                }.addOnFailureListener {
                    Toast.makeText(this, "Falha no upload: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun registroUsuario() {
        binding.BotaoRegistrar.setOnClickListener {
            val nome = binding.usernameEditText.text.toString()
            val email = binding.textoregistroEmail.text.toString()
            val senha = binding.passwordEditText.text.toString()
            val senha2 = binding.password2.text.toString()
            val cpf = "123124"
            var imagem = preferencesManager.userImage

            if(imagem.isNullOrEmpty()){
                imagem = "https://firebasestorage.googleapis.com/v0/b/imagepets-82fe7.appspot.com/o/Post%20Instagram%20Hoje%20n%C3%A3o%20teremos%20culto.png?alt=media&token=51cbe88f-02f2-47d2-8237-51f31d814e99"
            }
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
                            imagem
                        )
                    }
                    preferencesManager.userImage = null
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
            try {
                userApi.saveUser(novoUser)
                usuarioDao.cadastrarUsuario(novoUsuario)
                withContext(Main) {
                    startActivity(Intent(this@CadastroActivity, LoginActivity::class.java))
                    Toast.makeText(
                        this@CadastroActivity,
                        "Usuario Cadastrado!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Main) {
                    Toast.makeText(
                        this@CadastroActivity,
                        "Não é possível criar cadastro offline!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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