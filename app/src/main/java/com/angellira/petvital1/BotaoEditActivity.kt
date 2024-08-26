// EditProfileBottomSheet.kt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.angellira.petvital1.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditProfileDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_botao_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextEmail = view.findViewById<EditText>(R.id.textemaileditprofile)
        val editTextPhone = view.findViewById<EditText>(R.id.textTelefoneeditProfile)
        val buttonChooseImage = view.findViewById<EditText>(R.id.textimagemeditprofile)
        val buttonSave = view.findViewById<Button>(R.id.botaoconfirmaredicaoconta)

//        buttonChooseImage.setOnClickListener {
//            // Lógica para escolher uma imagem da galeria ou tirar uma foto
//        }

        buttonSave.setOnClickListener {
            // Obtenha os dados inseridos
            val newEmail = editTextEmail.text.toString()
            val newPhone = editTextPhone.text.toString()
            val newImage = buttonChooseImage.text.toString()
            // Lógica para salvar os novos dados
            dismiss() // Fechar o dialog quando o botão "Salvar" é pressionado
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
