package com.angellira.petvital1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.petvital1.databinding.ActivityLoginBinding
import com.angellira.petvital1.model.User
import com.angellira.petvital1.preferences.PreferencesManager

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
        verificaLogin()
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
                sharedPreferences(intent)
            } else {
                Toast.makeText(this, "Erro no login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun botaoEsqueciaSenha() {
        binding.button2esquecisenha.setOnClickListener {
            startActivity(Intent(this, EsqueciASenhaActivity::class.java))
        }
    }

    private fun botaoRegistro() {
        binding.BotaoRegistrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }

    private fun verificaLogin(){
        val estaLogado = preferencesManager.estaLogado
        if(estaLogado){
        startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun sharedPreferences(mainActivity: Intent) {
        preferencesManager.estaLogado = true
        startActivity(mainActivity)
        finish()
    }
}

