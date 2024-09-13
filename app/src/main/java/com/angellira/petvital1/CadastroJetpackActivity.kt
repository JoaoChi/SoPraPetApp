package com.angellira.petvital1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.angellira.petvital1.model.User
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.ui.theme.PetVital1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastroJetpackActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetVital1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    cadastro()
                }
            }
        }
    }
}

@Composable
fun cadastro() {

    var email by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(121, 213, 255, 255)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.petvital__2__removebg_preview),
                contentDescription = "Logo",
                Modifier.size(150.dp)

            )

            TextField(
                value = email,
                onValueChange = { newText ->
                    email = newText
                },
                label = { Text("Digite o seu Email") },
                modifier = Modifier.padding(16.dp),
                singleLine = true,
            )

            TextField(
                value = nome,
                onValueChange = { newText ->
                    nome = newText
                },
                label = { Text("Digite seu nome") },
                modifier = Modifier.padding(16.dp),
                singleLine = true,
            )

            TextField(
                value = cpf,
                onValueChange = { newText ->
                    cpf = newText
                },
                label = { Text("Digite seu CPF") },
                modifier = Modifier.padding(16.dp),
                singleLine = true,
            )

            TextField(
                value = senha,
                onValueChange = { newText ->
                    senha = newText
                },
                label = { Text("Digite sua senha") },
                modifier = Modifier.padding(16.dp),
                singleLine = true,
            )

            TextField(
                value = confirmarSenha,
                onValueChange = { newText ->
                    confirmarSenha = newText
                },
                label = { Text("Confirme sua senha") },
                modifier = Modifier.padding(16.dp),
                singleLine = true,
            )

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .height(40.dp)
                    .width(200.dp),
                containerColor = Color(3, 99, 143, 255),
                onClick = {

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val userApi = UsersApi.retrofitService

                            val novoUser = User(
                                name = nome,
                                email = email,
                                password = senha,
                                imagem = "",
                                cpf = cpf
                            )
                            if (nome.isNullOrEmpty() ||
                                email.isNullOrEmpty() ||
                                senha.isNullOrEmpty() ||
                                confirmarSenha.isNullOrEmpty() ||
                                cpf.isNullOrEmpty()
                            ) {
                                withContext(Main) {
                                    println("Preencha todos os campos!")
                                    Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else if (confirmarSenha != senha){
                                withContext(Main){
                                    Toast.makeText(context, "As senhas devem coinscidir!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{

                            userApi.saveUser(novoUser)

                            withContext(Main) {
                                val intent = Intent(context, LoginActivity::class.java)
                                context.startActivity(intent)
                                println("Cadastrado!")
                                Toast.makeText(context, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                            }

                        }} catch (e: Exception) {
                            withContext(Main) {
                                println("Erro na chamada de API: ${e.message}")
                                Toast.makeText(context, "Erro na api", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            ) {
                Text(
                    text = "Confirmar",
                    color = Color(255, 255, 255, 255),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    cadastro()
}