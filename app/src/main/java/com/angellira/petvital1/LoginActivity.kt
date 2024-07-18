package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.petvital1.databinding.ActivityLoginBinding
import com.angellira.petvital1.interfaces.Autenticator
import com.angellira.petvital1.model.User
import com.angellira.petvital1.preferences.PreferencesManager
import com.angellira.petvital1.preferences.preferenciaCadastro

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val cadastrado = User()

    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val intent = Intent(this, MainActivity::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        preferencesManager = PreferencesManager(this)
        sharedPreferences(intent)
        recebendoDados()
        funcaoVerificacaoLogin(intent)
        botaoRegistro()
        botaoEsqueciaSenha()
    }

    private fun recebendoDados() {
        val recebernome = intent.getStringExtra("nome")
        val recebergmail = intent.getStringExtra("gmail")
        val recebersenha = intent.getStringExtra("senha")

        cadastrado.username = recebernome.toString()
        cadastrado.email = recebergmail.toString()
        cadastrado.password = recebersenha.toString()
    }

    private fun funcaoVerificacaoLogin(intent: Intent) {
        val botaoLogin = binding.botaoLogin
        botaoLogin.setOnClickListener {

            val email = binding.textEmailLogin.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (cadastrado.authenticate(email = email, password = password)) {
                preferencesManager.isAuthenticated = true
                startActivity(intent)
            } else {
                Toast.makeText(this, "Erro no login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun botaoEsqueciaSenha() {
        binding.button2esquecisenha.setOnClickListener {
            startActivity(Intent(this, EsqueciASenha::class.java))
        }
    }

    private fun botaoRegistro() {
        binding.BotaoRegistrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }

    private fun sharedPreferences(mainActivity: Intent){
        val preferencesManager = PreferencesManager(this)
        val estaLogado = preferencesManager.estaLogado

        if (estaLogado) {
            startActivity(mainActivity)
            finish()
        }
    }
}
