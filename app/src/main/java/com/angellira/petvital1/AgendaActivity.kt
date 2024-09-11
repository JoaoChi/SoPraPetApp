package com.angellira.petvital1

import android.graphics.Paint.Style
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angellira.petvital1.ui.theme.PetVital1Theme

class AgendaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetVital1Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    app()
                }
            }
        }
    }
}

@Composable
fun app() {

    var valorCampo by remember {
        mutableStateOf("")
    }

    var valorCampo2 by remember {
        mutableStateOf("")
    }


    Column(
        Modifier.background(color = Color(3, 169, 244, 255)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Alcool ou gasolina?",
                style = TextStyle(
                    color = Color(255, 255, 255, 255),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,

                    )
            )

            if (valorCampo.isNotBlank() && valorCampo2.isNotBlank()) {
                val gasolina = valorCampo.toDouble() / valorCampo2.toDouble() > 0.7


                val eGasolina = if (gasolina) {
                    "Gasolina"
                } else {
                    "Alcool"
                }

                val cor = if(gasolina){
                    Color.Red
                }else{
                    Color.Green
                }

                Text(
                    text = eGasolina, style = TextStyle(
                        color = cor,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }


            TextField(value = valorCampo2,
                onValueChange = {
                    valorCampo2 = it
                },
                label = {
                    Text(text = "Gasolina")
                }

            )

            TextField(value = valorCampo,
                onValueChange = {
                    valorCampo = it
                },
                label = {
                    Text(text = "Alcool")
                }
            )
        }
    }
}

@Preview
@Composable
fun appPreview() {
    app()
}