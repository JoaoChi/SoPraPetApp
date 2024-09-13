package com.angellira.petvital1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angellira.petvital1.ui.theme.PetVital1Theme
import java.util.Calendar

class AgendaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetVital1Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    appEtoolbar()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun app() {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val options = listOf("Banho", "Tosa", "Veterinária")
    var expanded by remember { mutableStateOf(false) }
    var expandir by remember { mutableStateOf(false) }
    val horarios = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    var selectedText by remember { mutableStateOf((options[0])) }
    var selectedText2 by remember { mutableStateOf("Data") }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            selectedText2 = "$dayOfMonth/${month + 1}/$year"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )


    Column(
        Modifier.background(color = Color(121, 213, 255, 255)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(0.dp))

            Image(
                painter = painterResource(R.drawable.petvital__4__removebg_preview),
                contentDescription = "Logo",
                Modifier.size(100.dp)

            )

            Text(
                text = "SóPraPet",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W900,
                    color = Color(0xFF0162AF)
                )
            )

            Spacer(modifier = Modifier.height(60.dp))


            Text(
                text = "Qual Serviço Gostaria \nde agendar?",
                style = TextStyle(
                    color = Color(0, 0, 0, 255),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                    )
            )

            Spacer(modifier = Modifier.height(40.dp))


            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecione o serviço") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor()
                )


                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedText = option
                                expanded = false
                                Toast.makeText(context, option, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


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
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandir)
                    },
                    modifier = Modifier
                        .clickable { datePickerDialog.show()}
                            .padding(16.dp)
                )
                DropdownMenu(
                    expanded = expandir,
                    onDismissRequest = { expandir = false },
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

            Spacer(modifier = Modifier.height(20.dp))

            val context = LocalContext.current

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp),
                containerColor = Color(0xFF0162AF),
                onClick = {
                    val intent = Intent(context, PetshopsActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Text(
                    text = "Voltar",
                    color = Color(0xFF000000),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(0.dp))

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
                            color = Color(0, 0, 0, 255),
                            fontSize = (16.sp)
                        )
                    )
                    Text(
                        text = "Petshop Verificado!",
                        style = TextStyle(
                            color = Color(0, 0, 0, 255),
                            fontSize = (16.sp)
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myToolBar() {
    val context = LocalContext.current

    TopAppBar(
        title = { Text(text = "SóPraPet") },
        navigationIcon = {
            IconButton(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Menu"
                )
            }
        },

        actions = {
            IconButton(onClick = {
                val intent = Intent(context, CadastroJetpackActivity::class.java)
                context.startActivity(intent)
            })
            {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "Profile")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(10, 116, 201, 255),
            titleContentColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
    )
}

@Composable
fun appEtoolbar() {
    Scaffold(
        topBar = { myToolBar() },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = Color(121, 213, 255, 255)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                app()
            }
        }
    )
}


@Preview
@Composable
fun appPreview() {
    appEtoolbar()
}