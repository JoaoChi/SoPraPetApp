package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.petvital1.databinding.ActivityMinhacontaBinding
import com.angellira.petvital1.model.User
import com.angellira.petvital1.preferences.PreferencesManager
import com.angellira.petvital1.preferences.preferenciaCadastro
import com.angellira.petvital1.recyclerview.adapter.PreferenciasListAdapter

class MinhacontaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMinhacontaBinding
    private val dados = User()

    private lateinit var preferencesManager: PreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Intent(this, MinhacontaActivity::class.java)
        binding = ActivityMinhacontaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        preferencesManager = PreferencesManager(this)
        botaoVoltar()
        botaoPropaganda()
        botaoEditProfile()
        printPreferences()
        botaoDeslogarPreferences()

        val preferences = listOf(
            "Banho",
            "Tosa Máquina",
            "Tosa Tesoura",
            "Tosa higiênica",
            "Doença",
            "Medicamentos",
            "Tratamentos",
            "Produtos",
            "Ração",
            "Brinquedos",
            "Hidratação",
            "Escovação",
            "Tingimento",
            "Corte de Unhas",
            "Limpeza Ouvidos",

        )
        val adapter = PreferenciasListAdapter(
            preferencias = preferences
        )
        binding.textItensRecyclerview.adapter = adapter
    }

    private fun printPreferences() {

        dados.username = preferencesManager.username ?: dados.username
        val textNomeCadastro = binding.textnomecadastro
        textNomeCadastro.text = "Nome: ${dados.username}"

        dados.email = preferencesManager.email ?: dados.email
        val textEmailCadastro = binding.textemailcadastro
        textEmailCadastro.text = "Email: ${dados.email}"
    }

    private fun botaoDeslogarPreferences() {

        val buttonDeslogar = binding.buttonsair
        buttonDeslogar.setOnClickListener {
            preferencesManager.logout()
            val deslogarLogin = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(deslogarLogin)
            finish()
        }
    }

    private fun botaoVoltar() {
        binding.buttonVoltapaginainicial.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun botaoPropaganda() {
        binding.botaopetshop.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun botaoEditProfile() {
        binding.buttoneditprofile.setOnClickListener {
            startActivity(Intent(this, EditarPerfilActivity::class.java))
        }
    }
}
