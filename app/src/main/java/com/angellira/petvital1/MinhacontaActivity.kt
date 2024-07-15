package com.angellira.petvital1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

        botaoVoltar()
        botaoPropaganda()
        botaoEditProfile()
        printPreferences()
        botaoDeslogarPreferences()
    }

    private fun printPreferences() {

        val recebernome = intent.getStringExtra("nome")
        val recebergmail = intent.getStringExtra("gmail")

        dados.username = recebernome.toString()
        dados.email = recebergmail.toString()

        binding.textnomecadastro.text = "Usuario: \n " + recebernome
        binding.textemailcadastro.text = "E-mail: \n" + recebergmail
    }

    private fun botaoDeslogarPreferences() {
        val sharedPreferences = getSharedPreferences("Logou", MODE_PRIVATE)
        val buttonDeslogar = binding.buttonsair
        buttonDeslogar.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.clear().apply()
            editor.putBoolean("Logou", false).apply()
            startActivity(Intent(this, LoginActivity::class.java))
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
