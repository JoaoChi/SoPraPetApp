package com.angellira.petvital1

import android.graphics.Paint.Style
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angellira.petvital1.model.Pet
import com.angellira.petvital1.model.Petshop
import com.angellira.petvital1.ui.theme.PetVital1Theme
import kotlin.math.exp

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun app() {

    val options = listOf("Banho", "Tosa", "Veterinária")
    var expanded by remember { mutableStateOf(false) }
    var expandir by remember { mutableStateOf(false) }
    val horarios = listOf("1, 2, 3, 4, 5, 6, 7, 8, 9, 10")
    var selectedText by remember { mutableStateOf("Selecione o serviço") }
    var selectedText2 by remember { mutableStateOf("Selecione o horário") }

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
                text = "SóPraPet",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W900,
                    color = Color(0xFFFFC107)
                )
            )

            Spacer(modifier = Modifier.height(80.dp))


            Text(
                text = "Qual Serviço Gostaria \nde agendar?",
                style = TextStyle(
                    color = Color(255, 255, 255, 255),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,

                    )
            )

            if (valorCampo.isNotBlank() && valorCampo2.isNotBlank()) {
                val gasolina = valorCampo.toDouble() / valorCampo2.toDouble() > 0.7


                val eGasolina = if (gasolina) {
                    "Banho"
                } else {
                    "Tosa"
                }

                val cor = if (gasolina) {
                    Color.Red
                } else {
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

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecione o serviço") },
                    modifier = Modifier
                        .clickable { expanded = true },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )


                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(150.dp)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedText = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(expanded = expandir,
                onExpandedChange = { expandir = !expandir }
            )
            {
                TextField(
                    value = selectedText2,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(text = "Selecione a data")
                    },
                    modifier = Modifier
                        .clickable { expandir = true },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandir)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                DropdownMenu(expanded = expandir,
                    onDismissRequest = { expandir = false },
                    modifier = Modifier.width(150.dp)
                ) {
                    horarios.forEach { horarios ->
                        DropdownMenuItem(text = { Text(horarios) },
                            onClick = {
                                selectedText2 = horarios
                                expandir = false
                            })


                    }

                }
            }



            Spacer(modifier = Modifier.height(30.dp))

            Row {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                ) {
                    Image(
                        painter = painterResource(R.drawable.dia_mundial_do_gato__veterin_rio_cat_felinos_fevereiro_divertido_moderno_post_de_instagram),
                        contentDescription = "Perfil Usuario",
                        modifier = Modifier.fillMaxSize()
                    )

                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Icone Check",
                        tint = Color.Green,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)

                    )

                }
                Spacer(modifier = Modifier.width(8.dp))

                Column {


                    Text(
                        text = "Petchopp",
                        style = TextStyle(
                            color = Color(255, 255, 255, 255),
                            fontSize = (16.sp)
                        )
                    )
                    Text(
                        text = "Petshop Verificado!",
                        style = TextStyle(
                            color = Color(255, 255, 255, 255),
                            fontSize = (16.sp)
                        )
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun appPreview() {
    app()
}