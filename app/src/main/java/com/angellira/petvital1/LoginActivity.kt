package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.angellira.petvital1.Interfaces.autenticator
import com.angellira.petvital1.databinding.ActivityLoginBinding
import com.angellira.petvital1.model.User

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val cadastrado = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        JaestaLogado()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent = Intent(this, MainActivity::class.java)

        val botaoLogin = findViewById<Button>(R.id.botaoLogin)
        botaoLogin.setOnClickListener{

            cadastrado.email = findViewById<EditText>(R.id.textEmailLogin).text.toString()
            cadastrado.password = findViewById<EditText>(R.id.editTextPassword).text.toString()

            if(validateLogin(cadastrado.email, cadastrado.password)){

            }

            val nomeusuario = binding.textEmailLogin.text.toString()
            intent.putExtra("Usuario", cadastrado.username)
            startActivity(intent)
        }

        binding.BotaoRegistrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        binding.button2esquecisenha.setOnClickListener {
            startActivity(Intent(this, EsqueciASenha::class.java))
        }
    }


    private fun JaestaLogado() {
        val sharedPreferences = getPreferences(MODE_PRIVATE)

        val estaLogado = b(sharedPreferences)

        if (estaLogado) {
            val main = Intent(this, MainActivity::class.java)
            startActivity(main)
        }

        sharedPreferences.edit().putBoolean("Logou", true).apply()
    }

    private fun validateLogin(email: String, password: String): Boolean {
        if(cadastrado.email == email && cadastrado.password == password){
            return true
        }else{
            return false
        }
    }

    private fun b(sharedPreferences: SharedPreferences) =
        sharedPreferences.getBoolean("Logou", false)

}
