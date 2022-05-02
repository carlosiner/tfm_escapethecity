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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

//    companion object{
//        lateinit var usermail: String
//        lateinit var user: String
//        lateinit var providerSession: String
//    }

    private lateinit var auth: FirebaseAuth

    private var email by Delegates.notNull<String>()
//    private var user by Delegates.notNull<String>()
    private var pass by Delegates.notNull<String>()

    private lateinit var etEmail: EditText
//    private lateinit var etUser: EditText
    private lateinit var etPass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

//        etUser = findViewById(R.id.etUser)
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


    //TODO to change it with mail
    private fun loginUser(){
        email = etEmail.text.toString()
        pass = etPass.text.toString()
//        user = etUser.text.toString()

        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this,"Por favor, complete su email y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                    goHome(email, "email")

                } // TODO inicio por mail, no necesario
                else{
                    Toast.makeText(this,"Error en el acceso del usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Move to view: home
    private fun goHome(email: String, provider: String){
        RegistrationActivity.usermail = email
        RegistrationActivity.username = "dummy" // TODO obtener desde la BBDD
        RegistrationActivity.providerSession = provider

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Move to view: recovery
    private fun goRecovery(email:String) {
        val intent = Intent(this, RecoveryActivity::class.java)
        if (email.isNotEmpty()) intent.extras
        startActivity(intent)
    }
}