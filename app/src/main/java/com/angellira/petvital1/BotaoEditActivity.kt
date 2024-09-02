// EditProfileBottomSheet.kt
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.angellira.petvital1.MainActivity
import com.angellira.petvital1.MinhacontaActivity
import com.angellira.petvital1.R
import com.angellira.petvital1.network.UsersApi
import com.angellira.petvital1.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileDialogFragment : DialogFragment() {

    lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferencesManager = PreferencesManager(requireContext())
        return inflater.inflate(R.layout.activity_botao_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextNome = view.findViewById<EditText>(R.id.textemaileditprofile)
        val editTextPhone = view.findViewById<EditText>(R.id.textTelefoneeditProfile)
//        val buttonChooseImage = view.findViewById<EditText>(R.id.textimagemeditprofile)
        val buttonSave = view.findViewById<Button>(R.id.botaoconfirmaredicaoconta)

        buttonSave.setOnClickListener {
            val userApi = UsersApi.retrofitService

            var newName = editTextNome.text.toString()
            var newCpf = editTextPhone.text.toString()
//            var newImage = buttonChooseImage.text.toString()

            lifecycleScope.launch(IO) {
                val antigoUser = withContext(IO) {
                    userApi.getUsers(preferencesManager.userId.toString())
                }
//                if(newImage.isEmpty()){
//                    newImage = antigoUser.imagem
//                }
                if(newCpf.isEmpty()){
                    newCpf = antigoUser.cpf
                }
                if(newName.isEmpty()){
                    newName = antigoUser.name
                }

                userApi.editarPerfilUsuario(
                    antigoUser.email,
                    antigoUser.email,
                    newName,
                    newCpf,
                    antigoUser.imagem)
                    withContext(Main){
                        Toast.makeText(requireContext(), "Atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), MinhacontaActivity::class.java))
                    }
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}
