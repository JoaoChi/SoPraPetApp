package com.angellira.petvital1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
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

        val intent = Intent(this, MinhacontaActivity::class.java)
        binding = ActivityMinhacontaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        botaoVoltar()
        botaoPropaganda()
        botaoEditProfile()
        allPreferences()
    }

    private fun allPreferences() {

            val sharedPreferences = getSharedPreferences("Logou", MODE_PRIVATE)

            dados.email = sharedPreferences.getString("gmail", null).toString()
            val textEmail = binding.textnomecadastro
            dados.username = sharedPreferences.getString("nome", null).toString()
            val textNome = binding.textemailcadastro

            mostrarTexto(textNome, textEmail)
            val buttonDeslogar=binding.buttonsair
            buttonDeslogar.setOnClickListener{
                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                editor.clear().apply()
                editor.putBoolean("Logou", false).apply()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
    }

    private fun mostrarTexto(
        textNome: TextView,
        textEmail: TextView
    ){
        textNome.setText("Nome: " + dados.username)
        textEmail.setText("E-mail: " + dados.email)
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
