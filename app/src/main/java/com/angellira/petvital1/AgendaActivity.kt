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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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

    var showDialog by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val minDate = calendar.timeInMillis
    calendar.add(Calendar.MONTH, 2)
    val maxDate = calendar.timeInMillis

    val intent = Intent(context, AgendaActivity::class.java)

    val servicos = intent.getStringExtra("servicos")?: "Banho"
    val options = listOf("$servicos")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf((options[0])) }
    var selectedText2 by remember { mutableStateOf("Data") }

    val datePickerDialog = DatePickerDialog(
        context,
        R.style.CustomDatePickerDialog,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            selectedText2 = "$dayOfMonth/${month + 1}/$year"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.minDate = minDate
        datePicker.maxDate = maxDate
    }


    Column(
        Modifier.background(color = Color(69, 143, 255, 255)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                    color = Color(0xFFFFFFFF)
                )
            )

            Text(
                text = "Qual Serviço Gostaria \nde agendar?",
                style = TextStyle(
                    color = Color(255, 255, 255, 255),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                    )
            )

            Spacer(modifier = Modifier.height(20.dp))

            val focusRequester = remember { FocusRequester() }

            ListItem(
                headlineContent = { Text("Serviço") },
                supportingContent = { Text(text = "Selecione o serviço")},
                leadingContent = {
                    Icon(
                        Icons.Filled.Build,
                        contentDescription = "Data do agendamento",
                    )
                },
                modifier = Modifier
                    .clickable {
                        showDialog = true
                    }
                    .padding(26.dp)
                    .focusRequester(focusRequester)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
            ) {
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

            ListItem(
                headlineContent = { Text("Data") },
                supportingContent = { Text(text = "Selecione a data")},
                leadingContent = {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = "Data do agendamento",
                    )
                },
                        modifier = Modifier
                            .clickable { datePickerDialog.show() }
                            .padding(26.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            val context = LocalContext.current

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp),
                containerColor = Color(0xFFF6C64B),
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

                    val pegandoNome = intent.getStringExtra("nome_petshop")?: "Nome petshop"

                    Text(
                        text = "$pegandoNome",
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

        ServiceSelectionDialog(
            showDialog = showDialog,
            selectedOption = selectedText,
            onDismiss = { showDialog = false },
            onSelect = {option ->
                selectedOption = option
                showDialog = false
            }
        )
    }
}

@Composable
fun ServiceSelectionDialog(
    showDialog: Boolean,
    selectedOption: String?,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Selecione um serviço") },
            text = {
                Column {
                    listOf("Veterinária", "Tosa", "Banho").forEach { service ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(service) }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = service == selectedOption,
                                onClick = { onSelect(service) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = service)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
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
            containerColor = Color(246, 198, 75, 255),
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black,
            navigationIconContentColor = Color.Black
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
                    .background(color = Color(69, 143, 255, 255)),
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