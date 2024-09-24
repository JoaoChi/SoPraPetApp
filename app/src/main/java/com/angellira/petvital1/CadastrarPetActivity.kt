package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.angellira.petvital1.databinding.ActivityCadastrarPetBinding
import com.angellira.petvital1.model.Pet
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastrarPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarPetBinding
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundociano)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundociano)
        cadastrarPet()
        botaoVoltar()
        layoutVisibleInvisible()
        pegarImagemPet()
        preferencesManager = PreferencesManager(this)
    }


    private fun layoutVisibleInvisible() {

        binding.buttonProximo.visibility = INVISIBLE

        binding.editNomePet.addTextChangedListener(/* watcher = */ object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonProximo.visibility = if (s.isNullOrBlank()) {
                    INVISIBLE
                } else {
                    VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.buttonProximo.setOnClickListener {
            binding.textoNome.visibility = INVISIBLE
            binding.editNomePet.visibility = INVISIBLE
            binding.textoRaca.visibility = VISIBLE
            binding.editRacaPet.visibility = VISIBLE
            binding.buttonProximo.visibility = INVISIBLE

            binding.editRacaPet.addTextChangedListener(/* watcher = */ object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding.buttonProximo2.visibility = if (s.isNullOrBlank()) {
                        INVISIBLE
                    } else {
                        VISIBLE
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
        binding.buttonProximo2.setOnClickListener {
            binding.textoRaca.visibility = INVISIBLE
            binding.editRacaPet.visibility = INVISIBLE
            binding.textoPeso.visibility = VISIBLE
            binding.editPeso.visibility = VISIBLE
            binding.buttonProximo2.visibility = INVISIBLE

            binding.editPeso.addTextChangedListener(/* watcher = */ object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding.buttonProximo3.visibility = if (s.isNullOrBlank()) {
                        INVISIBLE
                    } else {
                        VISIBLE
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
        binding.buttonProximo3.setOnClickListener {
            binding.textoPeso.visibility = INVISIBLE
            binding.editPeso.visibility = INVISIBLE
            binding.buttonProximo3.visibility = INVISIBLE
            binding.editIdade.visibility = VISIBLE
            binding.textoIdade.visibility = VISIBLE

            binding.editIdade.addTextChangedListener(/* watcher = */ object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding.buttonProximo4.visibility = if (s.isNullOrBlank()) {
                        INVISIBLE
                    } else {
                        VISIBLE
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
        binding.buttonProximo4.setOnClickListener{
            binding.buttonProximo4.visibility = INVISIBLE
            binding.editIdade.visibility = INVISIBLE
            binding.textoIdade.visibility = INVISIBLE
            binding.imagemPet.visibility = VISIBLE
            binding.textoImagem.visibility = VISIBLE
            binding.buttonSalvarPet.visibility = VISIBLE
        }
    }

    private fun pegarImagemPet(){
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val imageUri = uri

                    uploadImageToFirebase(imageUri)
                    binding.textoAviso.visibility = VISIBLE
                    Toast.makeText(this@CadastrarPetActivity, "Imagem Selecionada.", Toast.LENGTH_SHORT).show()
                } else {
                    binding.textoAviso.visibility = INVISIBLE
                    binding.buttonSalvarPet.visibility = VISIBLE
                    binding.imagemPet.visibility = VISIBLE
                    Toast.makeText(this@CadastrarPetActivity, "Nenhuma imagem Selecionada", Toast.LENGTH_SHORT).show()
                }
            }
        binding.imagemPet.setOnClickListener {
            binding.imagemPet.visibility = INVISIBLE
            binding.buttonSalvarPet.visibility = INVISIBLE
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?) {
        if (imageUri != null) {
            val storage = Firebase.storage
            val storageRef = storage.reference

            val imagesRef = storageRef.child("images/pets/${imageUri.lastPathSegment}")

            val uploadTask = imagesRef.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->

                    val downloadUrl = uri.toString()
                    preferencesManager.petImage = downloadUrl

                    Toast.makeText(this, "Imagem carregada!", Toast.LENGTH_SHORT).show()
                    binding.textoAviso.visibility = INVISIBLE
                    binding.buttonSalvarPet.visibility = VISIBLE
                    binding.imagemPet.visibility = VISIBLE

                }.addOnFailureListener {
                    Toast.makeText(this, "Falha no upload: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setupView() {
        binding = ActivityCadastrarPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun botaoVoltar() {
        binding.voltarMain.setOnClickListener {
            startActivity(Intent(this@CadastrarPetActivity, MainActivity::class.java))
        }
    }

    private fun cadastrarPet() {
        binding.buttonSalvarPet.setOnClickListener {

            val nome = binding.editNomePet.text.toString()
            val description = binding.editRacaPet.text.toString()
            val peso = binding.editPeso.text.toString()
            val idade = binding.editIdade.text.toString()
            var imagem = preferencesManager.petImage

            if(imagem.isNullOrEmpty()){
                imagem = "https://firebasestorage.googleapis.com/v0/b/imagepets-82fe7.appspot.com/o/Post%20Instagram%20Hoje%20n%C3%A3o%20teremos%20culto.png?alt=media&token=51cbe88f-02f2-47d2-8237-51f31d814e99"
            }
            if (nome.isNotEmpty() &&
                description.isNotEmpty() &&
                peso.isNotEmpty() &&
                idade.isNotEmpty()
            ) {
                lifecycleScope.launch {
                    registrarPet(
                        this@CadastrarPetActivity,
                        nome, description, peso, idade, imagem.toString()
                    )
                }
                preferencesManager.petImage = null
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun registrarPet(
        context: Context,
        nome: String,
        description: String,
        peso: String,
        idade: String,
        imagem: String
    ) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "Petvital.db"
        ).build()

        val petDao = db.petDao()
        val petApi = UsersApi.retrofitService

        val novoPet = Pet(
            name = nome,
            descricao = description,
            peso = peso,
            idade = idade,
            imagem = imagem
        )
        withContext(IO) {
            try {
                petApi.savePets(novoPet)
                petDao.cadastrarPet(novoPet)
                withContext(Main) {
                    startActivity(Intent(this@CadastrarPetActivity, MainActivity::class.java))
                    Toast.makeText(
                        this@CadastrarPetActivity,
                        "Pet Cadastrado!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Main) {
                    Toast.makeText(
                        this@CadastrarPetActivity,
                        "Não é possível cadastrar Pets offline!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}