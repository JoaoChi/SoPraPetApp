package com.angellira.petvital1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.angellira.petvital1.Interfaces.autenticator
import com.angellira.petvital1.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MainActivity::class.java)


        binding.botaoLogin.setOnClickListener {
            val email = binding.textEmailLogin.text.toString()
            intent.putExtra("email", email)
            startActivity(intent)
        }

        binding.BotaoRegistrar.setOnClickListener{
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        binding.button2esquecisenha.setOnClickListener {
            startActivity(Intent(this, EsqueciASenha::class.java))
        }
    }
}
