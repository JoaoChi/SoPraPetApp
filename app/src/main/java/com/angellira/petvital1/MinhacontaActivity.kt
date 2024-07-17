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

class MinhacontaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMinhacontaBinding
    val dados = User()

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

        botaoVoltar()
        botaoPropaganda()
        botaoEditProfile()
        printPreferences()
        botaoDeslogarPreferences()
    }

    private fun printPreferences() {

        val sharedPreferences = getSharedPreferences(preferenciaCadastro, Context.MODE_PRIVATE)
        dados.username = sharedPreferences.getString("nome", dados.username).toString()
        val textNomeCadastro = binding.textnomecadastro
        textNomeCadastro.text = "Nome: ${dados.username}"

        dados.email = sharedPreferences.getString("gmail", dados.email).toString()
        val textEmailCadastro = binding.textemailcadastro
        textEmailCadastro.text = "Email: ${dados.email}"
    }

    private fun botaoDeslogarPreferences() {
        val sharedPreferences = getSharedPreferences(preferenciaCadastro, MODE_PRIVATE)
        val buttonDeslogar = binding.buttonsair
        buttonDeslogar.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putBoolean("cadastro", false).clear().apply()
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
