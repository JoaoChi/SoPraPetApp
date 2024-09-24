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
import com.angellira.petvital1.databinding.ActivityCadastrarPetshopBinding
import com.angellira.petvital1.model.Petshop
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastrarPetshopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarPetshopBinding
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundo)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundo)
        cadastrarPetshop()
        pegarImagemPet()
        preferencesManager = PreferencesManager(this)
    }

    private fun setupView() {
        binding = ActivityCadastrarPetshopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun pegarImagemPet(){
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val imageUri = uri

                    uploadImageToFirebase(imageUri)
                    Toast.makeText(this@CadastrarPetshopActivity, "Imagem selecionada", Toast.LENGTH_SHORT).show()
                    binding.textoAviso.visibility = VISIBLE
                } else {
                    Toast.makeText(this@CadastrarPetshopActivity, "Erro", Toast.LENGTH_SHORT).show()
                    binding.buttonSalvarPetshop.visibility = VISIBLE
                    binding.buttonAdicionarImagem.visibility = VISIBLE
                }
            }
        binding.buttonAdicionarImagem.setOnClickListener {
            binding.buttonAdicionarImagem.visibility = INVISIBLE
            binding.buttonSalvarPetshop.visibility = INVISIBLE
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?) {
        if (imageUri != null) {
            val storage = Firebase.storage
            val storageRef = storage.reference

            val imagesRef = storageRef.child("images/petshop/${imageUri.lastPathSegment}")

            val uploadTask = imagesRef.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->

                    val downloadUrl = uri.toString()
                    preferencesManager.petshopImage = downloadUrl

                    Toast.makeText(this, "Upload bem-sucedido", Toast.LENGTH_SHORT).show()
                    binding.textoAviso.visibility = INVISIBLE
                    binding.buttonSalvarPetshop.visibility = VISIBLE
                    binding.buttonAdicionarImagem.visibility = VISIBLE

                }.addOnFailureListener {
                    Toast.makeText(this, "Falha no upload: ${it.message}", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }


    private fun cadastrarPetshop() {
        binding.buttonSalvarPetshop.setOnClickListener {

            val nome = binding.nomePetshop.text.toString()
            val description = binding.descricaoPetshop.text.toString()
            val localizacao = binding.localizacaoPetshop.text.toString()
            val servicos = binding.servicosPetshop.text.toString()
            var imagem = preferencesManager.petshopImage
            val cnpj = binding.textCnpj.text.toString()

            if(imagem.isNullOrEmpty()){
                imagem = "https://firebasestorage.googleapis.com/v0/b/imagepets-82fe7.appspot.com/o/Post%20Instagram%20Hoje%20n%C3%A3o%20teremos%20culto.png?alt=media&token=51cbe88f-02f2-47d2-8237-51f31d814e99"
            }
            if (nome.isNotEmpty() &&
                description.isNotEmpty() &&
                localizacao.isNotEmpty() &&
                servicos.isNotEmpty()
            ) {
                lifecycleScope.launch(IO) {
                    registrarPetshop(
                        this@CadastrarPetshopActivity,
                        nome, description, localizacao, servicos, imagem.toString(), cnpj
                    )
                }
                preferencesManager.petshopImage = null
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun registrarPetshop(
        context: Context,
        nome: String,
        description: String,
        localizacao: String,
        servicos: String,
        imagem: String,
        cnpj: String
    ) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "Petvital.db"
        ).build()

        val petshopDao = db.petshopDao()
        val petshopApi = UsersApi.retrofitService

        val petshopExiste = withContext(Main) {
            petshopDao.pegarCnpj(cnpj)
        }

        if (petshopExiste != null) {
            withContext(Main) {
                Toast.makeText(this@CadastrarPetshopActivity, "CNPJ já existe!", Toast.LENGTH_SHORT)
                    .show()
            }
            return
        }


        val novoPetshop = Petshop(
            name = nome,
            descricao = description,
            localizacao = localizacao,
            servicos = servicos,
            imagem = imagem,
            cnpj = cnpj
        )
        withContext(IO) {
            try {
                petshopApi.savePetshop(novoPetshop)
                petshopDao.cadastrarPetshop(novoPetshop)
                withContext(Main) {
                    startActivity(
                        Intent(
                            this@CadastrarPetshopActivity,
                            PetshopsActivity::class.java
                        )
                    )
                    Toast.makeText(
                        this@CadastrarPetshopActivity,
                        "Petshop Cadastrado!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Main) {
                    Toast.makeText(
                        this@CadastrarPetshopActivity,
                        "Não é possível cadastrar Petshops offline!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

