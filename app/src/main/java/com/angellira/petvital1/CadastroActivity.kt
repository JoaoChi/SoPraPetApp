package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.petvital1.databinding.ActivityCadastroBinding
import com.angellira.petvital1.databinding.ActivityMainBinding
import com.angellira.petvital1.model.User

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        funcPegarDadosRegistro()
    }

    private fun botaoRegistrar() {

        val loginIntent = Intent(this, LoginActivity::class.java)

        loginIntent.putExtra("nome", cadastro.username)
        loginIntent.putExtra("gmail", cadastro.email)
        loginIntent.putExtra("senha", cadastro.password)

        startActivity(loginIntent)
    }

    val cadastro = User()

    private fun funcPegarDadosRegistro() {

        val botaoRegisto = findViewById<Button>(R.id.BotaoRegistrar)
        botaoRegisto.setOnClickListener {

            cadastro.email = binding.textoregistroEmail.text.toString()
            cadastro.password = binding.passwordEditText.text.toString()
            cadastro.username = binding.usernameEditText.text.toString()

            if (validacaoInput(cadastro.username, cadastro.email, cadastro.password)) {
                val user = User(cadastro.username, cadastro.email, cadastro.password)
                registerUser(user)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validacaoInput(username: String, email: String, password: String): Boolean {
        return username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()

    }

    private fun registerUser(cadastro: User) {

        val sharedPreferences = getSharedPreferences(preferenciaCadastro, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("gmail", cadastro.email)
            putString("senha", cadastro.password)
            putString("nome", cadastro.username)
            apply()
        }
        Log.d("RegisterActivity", "Usuário registrado: $cadastro")
        botaoRegistrar()
    }
}
