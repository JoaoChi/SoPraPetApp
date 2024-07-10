package com.angellira.petvital1

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

        val botaoRegisto = findViewById<Button>(R.id.BotaoRegistrar)
        botaoRegisto.setOnClickListener{

            val cadastro = User()
            val intent = (Intent(this, CadastroActivity::class.java))

            cadastro.email = findViewById<EditText>(R.id.textoregistroEmail).text.toString()
            cadastro.password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            cadastro.username = findViewById<EditText>(R.id.usernameEditText).text.toString()

            val estaRegistrado = registerUser()

            if(estaRegistrado){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Cadastro Falhou, tente novamente.", Toast.LENGTH_SHORT).show()
            }

        }



        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BotaoRegistrar.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
    private fun registerUser(cadastro: User): Boolean {

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        cadastro.email

        return true
    }
}
