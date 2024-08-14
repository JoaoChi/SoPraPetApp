package com.angellira.petvital1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.petvital1.databinding.ActivityCadastroBinding
import com.angellira.petvital1.databinding.ActivityEsqueciAsenhaBinding
import com.angellira.petvital1.model.Usuario
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.launch

class EsqueciASenhaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEsqueciAsenhaBinding
    private lateinit var preferencesManager: PreferencesManager
    private val users = UsersApi.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundo)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundo)
        preferencesManager = PreferencesManager(this)
        botaoRedefinir()
//        editSenha()
    }

//    private fun editSenha() {
//        binding.botaoRedefinirEVoltar.setOnClickListener {
//            lifecycleScope.launch {
//                val idUsuario = preferencesManager.userId
//                if (idUsuario.isNullOrEmpty()) {
//                    Toast.makeText(
//                        this@EsqueciASenhaActivity,
//                        "Id não encontrado.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@launch
//                }
//
//                try {
//                    val usuarios = users.getUser(idUsuario.toString())
//                    val senhaAtual = usuarios.password
//                    val nome = usuarios.name
//                    val email = usuarios.email
//                    val idAntigo = usuarios.uid
//                    val imagem = ""
//
//                    val novaSenha = binding.editTextNumberPassword2.text.toString()
//                    val novaSenha2 = binding.editTextNumberPassword2.text.toString()
//
//                    if (novaSenha != novaSenha2) {
//                        Toast.makeText(
//                            this@EsqueciASenhaActivity,
//                            "Senhas não coincidem!",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else if (novaSenha == senhaAtual) {
//                        Toast.makeText(
//                            this@EsqueciASenhaActivity,
//                            "Senha atual deve ser diferente da antiga.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else if (novaSenha.isNullOrEmpty() || novaSenha2.isNullOrEmpty()) {
//                        Toast.makeText(
//                            this@EsqueciASenhaActivity,
//                            "Preencha os dois campos!",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        val editarSenha = Usuario(idAntigo, nome, email, novaSenha, imagem)
//                        val editou = users.editUser(idUsuario, editarSenha)
//                        if (editou.isSuccessful) {
//                            Toast.makeText(
//                                this@EsqueciASenhaActivity,
//                                "Editado com sucesso!",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            startActivity(
//                                Intent(
//                                    this@EsqueciASenhaActivity,
//                                    EditarPerfilActivity::class.java
//                                )
//                            )
//                        } else {
//                            Toast.makeText(
//                                this@EsqueciASenhaActivity,
//                                "Erro inesperado!",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(
//                        this@EsqueciASenhaActivity,
//                        "Erro ao buscar usuario!",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }

    private fun botaoRedefinir() {
        binding.botaoRedefinirEVoltar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityEsqueciAsenhaBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}