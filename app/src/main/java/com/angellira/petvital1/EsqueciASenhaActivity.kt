package com.angellira.petvital1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.angellira.petvital1.databinding.ActivityEsqueciAsenhaBinding

class EsqueciASenhaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEsqueciAsenhaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEsqueciAsenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        botaoRedefinir()
    }

    private fun botaoRedefinir() {
        binding.botaoRedefinirEVoltar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}