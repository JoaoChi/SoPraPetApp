package com.angellira.petvital1

import EditProfileDialogFragment
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import coil.load
import com.angellira.petvital1.database.AppDatabase
import com.angellira.petvital1.databinding.ActivityMinhacontaBinding
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class MinhacontaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMinhacontaBinding
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var imageView: ImageView
    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundoazul)
        setSupportActionBar(findViewById(R.id.barra_tarefas))
        preferencesManager = PreferencesManager(this)
        botaoDeslogarPreferences()
        lifecycleScope.launch {
            pegarDadosUser(this@MinhacontaActivity)
        }
        botaoEsqueciaSenha()
        botaoeditarConta()
    }

    private fun botaoeditarConta() {
        val botaoeditarConta = binding.editProfile
        botaoeditarConta.setOnClickListener {
            val dialogFragment = EditProfileDialogFragment()
            dialogFragment.show(supportFragmentManager, "EditProfileDialogFragment")
        }
    }

    private fun setupView() {
        Intent(this, MinhacontaActivity::class.java)
        binding = ActivityMinhacontaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

//    private fun pegarImagem() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, PICK_IMAGE_REQUEST)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST
//            && resultCode == Activity.RESULT_OK
//            && data != null
//        ) {
//            val imageUri = data.data
//
//            imagemBase64 = encodeImageToBase64(imageUri!!)
//            trocarfoto()
//        }
//    }
//
//    fun encodeImageToBase64(imageUri: Uri): String? {
//        val imageStream = contentResolver.openInputStream(imageUri)
//        val bitmap = BitmapFactory.decodeStream(imageStream)
//
//        if (bitmap == null) {
//            Toast.makeText(this@MinhacontaActivity, "erro", Toast.LENGTH_SHORT).show()
//        }
//
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//        val imageBytes = byteArrayOutputStream.toByteArray()
//
//        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
//    }

    private suspend fun pegarDadosUser(context: Context) {
        val email = preferencesManager.userId
        lifecycleScope.launch(IO) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "Petvital.db"
            ).build()

            try {
            val userApi = UsersApi.retrofitService
            val user = userApi.getUsers(email.toString())

            if (user.name.isNotEmpty()) {
                withContext(Main) {
                    binding.textNome.text = user.name
                    binding.textCpf.text = user.cpf
                    binding.textTelefone.text = user.password

                    }
            }
            } catch (e: Exception) {

                val usuarioDao = db.usuarioDao()
                val usuario = usuarioDao.pegarEmailUsuario(email.toString())
                withContext(Main) {
                    binding.textNome.text = usuario!!.name
                    binding.textCpf.text = usuario.cpf
                    binding.textTelefone.text = usuario.password
                }
            }
        }
            val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imageOpen.load(uri)
                    val imageUri = uri
                    uploadImageToFirebase(imageUri)
                    Toast.makeText(this@MinhacontaActivity, "Deu boa", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MinhacontaActivity, "Erro", Toast.LENGTH_SHORT).show()
                }
            }
        binding.trocarimagem.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

//    private fun dowloadUrl(){
//        val email = preferencesManager.userId
//        val imageView = binding.imageOpen
//        val imageRef = storage.reference.child("images/$email/${imageUri.lastPathSegment}")
//    }


    private fun uploadImageToFirebase(imageUri: Uri?) {
        if (imageUri != null) {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val emailImg = preferencesManager.userId

            val imagesRef = storageRef.child("images/$emailImg/${imageUri.lastPathSegment}")

            val uploadTask = imagesRef.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                Toast.makeText(this, "Upload bem-sucedido", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Falha no upload: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun trocarfoto() {
        val email = preferencesManager.userId
        lifecycleScope.launch(IO) {
            try {
                val userApi = UsersApi.retrofitService
                val user = userApi.getUsers(email.toString())

                userApi.editarPerfilUsuario(
                    user.email,
                    user.email,
                    user.name,
                    user.cpf,
                    ""
                )
                withContext(Main) {
                    Toast.makeText(
                        this@MinhacontaActivity,
                        "Atualizado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@MinhacontaActivity, MainActivity::class.java))
                }

            }catch (e: Exception){
                withContext(Main){
                    Toast.makeText(this@MinhacontaActivity, "Não pode atualizar seus dados offline!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun botaoDeslogarPreferences() {

        val buttonDeslogar = binding.buttonsair
        buttonDeslogar.setOnClickListener {
            showConfirmationDialog(
                title = "Deseja sair?",
                message = "Certeza que deseja deslogar?",
                positiveAction = {
                    Toast.makeText(this, "Deslogando", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            this@MinhacontaActivity,
                            LoginActivity::class.java
                        )
                    )
                    preferencesManager.estaLogado = false
                    finishAffinity()
                }
            )
        }
    }

    private fun botaoEsqueciaSenha() {
        binding.button2esquecisenha.setOnClickListener {
            startActivity(Intent(this, EsqueciASenhaActivity::class.java))
        }
    }

    private fun showPopupMenu(view: View) {

        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.inicio -> {
                    Toast.makeText(this, "Voltando ao início", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MinhacontaActivity, MainActivity::class.java))
                    true
                }

                R.id.perfil -> {
                    Toast.makeText(this, "Já está no perfil", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.ajuda -> {
                    Toast.makeText(this, "Petshops", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MinhacontaActivity, PetshopsActivity::class.java))
                    true
                }

                R.id.config -> {
                    Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MinhacontaActivity, EditarPerfilActivity::class.java))
                    true
                }

                R.id.sair -> {
                    showConfirmationDialog(
                        title = "Deseja sair?",
                        message = "Certeza que deseja deslogar?",
                        positiveAction = {
                            Toast.makeText(this, "Deslogando", Toast.LENGTH_SHORT).show()
                            startActivity(
                                Intent(
                                    this@MinhacontaActivity,
                                    LoginActivity::class.java
                                )
                            )
                            preferencesManager.estaLogado = false
                            finishAffinity()
                        }
                    )
                    true
                }

                R.id.privacidade -> {
                    Toast.makeText(this, "Sem página ainda", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showConfirmationDialog(title: String, message: String, positiveAction: () -> Unit) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton("Sim") { dialog, _ ->
            positiveAction.invoke()
            dialog.dismiss()
        }

        builder.setNegativeButton("Não") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.voltarPagina -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }

            R.id.configs -> {
                showPopupMenu(findViewById(R.id.configs))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
