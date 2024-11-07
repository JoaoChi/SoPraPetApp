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
import com.angellira.petvital1.databinding.ActivityCadastrarPetshopBinding
import com.angellira.petvital1.model.Pet
import com.angellira.petvital1.model.Petshop
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class CadastrarPetshopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarPetshopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundo)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundo)
        cadastrarPetshop()
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

        binding.cadastrarPetshop.load(R.drawable.hit_the_follow_button_now_, imageLoader)

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

    private fun cadastrarPetshop() {
        binding.buttonSalvarPetshop.setOnClickListener {

            val nome = binding.nomePetshop.text.toString()
            val description = binding.descricaoPetshop.text.toString()
            val localizacao = binding.localizacaoPetshop.text.toString()
            val servicos = binding.servicosPetshop.text.toString()
            val imagem = binding.imagemPetshop.text.toString()
            val cnpj = binding.textCnpj.text.toString()

            if (nome.isNotEmpty() &&
                description.isNotEmpty() &&
                localizacao.isNotEmpty() &&
                servicos.isNotEmpty() &&
                imagem.isNotEmpty()
            ) {
                lifecycleScope.launch(IO) {
                    registrarPetshop(
                        this@CadastrarPetshopActivity,
                        nome, description, localizacao, servicos, imagem, cnpj
                    )
                    withContext(Main) {
                        Toast.makeText(this@CadastrarPetshopActivity, "Cadastrado", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@CadastrarPetshopActivity, PetshopsActivity::class.java))
                    }
                }
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
                Toast.makeText(this@CadastrarPetshopActivity, "CNPJ já existe!", Toast.LENGTH_SHORT).show()
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
//            petshopApi.savePetshop(novoPetshop)
            petshopDao.cadastrarPetshop(novoPetshop)
        }
    }
}
