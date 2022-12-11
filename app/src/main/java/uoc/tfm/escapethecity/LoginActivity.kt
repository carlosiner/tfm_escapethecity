package uoc.tfm.escapethecity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionProvider
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uoc.tfm.escapethecity.data.User
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class LoginActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPass = findViewById(R.id.etPass)
    }

    override fun onBackPressed() {
        finish()
    }

    fun login(view: View){
        loginUser()
    }

    // Move to view: recovery
    fun forgotPass(view: View){
        goRecovery(etEmail.toString())
    }


    private fun loginUser(){
        val emailStr = etEmail.text.toString()
        val passStr = etPass.text.toString()

        if (emailStr.isEmpty() || passStr.isEmpty()){
            Toast.makeText(this,"Por favor, complete su email y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(emailStr, passStr)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    loadUser()
                    Toast.makeText(this, "¡Hola de nuevo!", Toast.LENGTH_SHORT).show()
                    //mailER = emailStr
                    goHome()
                }
                else{
                    Toast.makeText(this,"Error en el acceso del usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Move to view: home
    private fun goHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Move to view: recovery
    private fun goRecovery(email:String) {
        val intent = Intent(this, RecoveryActivity::class.java)
        if (email.isNotEmpty()) intent.extras
        startActivity(intent)
    }
}