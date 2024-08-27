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
import androidx.room.Room
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.angellira.petvital1.database.AppDatabase
import com.angellira.petvital1.databinding.ActivityCadastrarPetBinding
import com.angellira.petvital1.model.Pet
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class CadastrarPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarPetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundociano)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundociano)
        cadastrarPet()
        imageLoad()
    }

    private fun imageLoad() {
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        binding.cadastrarPet.load(R.drawable.cadastrar_pet, imageLoader)

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

    private fun cadastrarPet() {
        binding.buttonSalvarPet.setOnClickListener {

            val nome = binding.editNomePet.text.toString()
            val description = binding.editRacaPet.text.toString()
            val peso = binding.editPeso.text.toString()
            val idade = binding.editIdade.text.toString()
            val imagem = binding.editImagem.text.toString()

            if (nome.isNotEmpty() &&
                description.isNotEmpty() &&
                peso.isNotEmpty() &&
                idade.isNotEmpty() &&
                imagem.isNotEmpty()
            ) {
                lifecycleScope.launch(IO) {
                    registrarPet(
                        this@CadastrarPetActivity,
                        nome, description, peso, idade, imagem
                    )
                    withContext(Main) {
                        Toast.makeText(this@CadastrarPetActivity, "Cadastrado", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@CadastrarPetActivity, MainActivity::class.java))
                    }
                }
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
            petApi.savePets(novoPet)
            petDao.cadastrarPet(novoPet)
        }
    }
}

