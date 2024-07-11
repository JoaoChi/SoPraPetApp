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

        FuncPegarDadosRegistro()
        botaoRegistrar()
    }

    private fun botaoRegistrar() {
        binding.BotaoRegistrar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    val cadastro = User()

    private fun FuncPegarDadosRegistro() {
        intent.putExtra("nome", cadastro.username)
        intent.putExtra("gmail", cadastro.email)
        intent.putExtra("senha", cadastro.password)
        val botaoRegisto = findViewById<Button>(R.id.BotaoRegistrar)
        botaoRegisto.setOnClickListener {


            cadastro.email = findViewById<EditText>(R.id.textoregistroEmail).text.toString()
            cadastro.password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            cadastro.username = findViewById<EditText>(R.id.usernameEditText).text.toString()

            if (ValidacaoInput(cadastro.username, cadastro.email, cadastro.password)) {
                val user = User(cadastro.username, cadastro.email, cadastro.password)
                registerUser(user)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun ValidacaoInput(username: String, email: String, password: String): Boolean {
        return username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()

    }

    private fun registerUser(cadastro: User) {

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("email", cadastro.email)
            putString("senha", cadastro.password)
            putString("usuario", cadastro.username)
            apply()
        }
        Log.d("RegisterActivity", "Usuário registrado: $cadastro")
    }
}
