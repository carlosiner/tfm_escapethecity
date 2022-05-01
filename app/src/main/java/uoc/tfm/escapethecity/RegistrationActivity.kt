package uoc.tfm.escapethecity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class RegistrationActivity : AppCompatActivity() {
    companion object{
        lateinit var usermail: String
        lateinit var username: String
        lateinit var providerSession: String
    }

    private lateinit var auth: FirebaseAuth

    private var email by Delegates.notNull<String>()
    private var user by Delegates.notNull<String>()
    private var pass by Delegates.notNull<String>()

    private lateinit var etEmail: EditText
    private lateinit var etUser: EditText
    private lateinit var etPass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etUser = findViewById(R.id.etUser)
        etPass = findViewById(R.id.etPass)

    }
    // Maintain the user session if present
    public override fun onStart() {
        super.onStart()
        val activeUser = auth.currentUser
        if (activeUser != null) goHome(activeUser.email.toString(), activeUser.providerId)
    }

    override fun onBackPressed() { // TODO
//        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags =Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    // New user registration
    fun registration(view: View){
        registerUser()
    }

    // Move to view: login
    fun login(view: View){
        goLogin()
    }




    /* Private functions */

    // Register a new user and move to view: home
    private fun registerUser(){
        email = etEmail.text.toString()
        pass = etPass.text.toString()
        user = etUser.text.toString()

        // Check the password size
        if (pass.length<6){
            Toast.makeText(this,"La contraseña es demasiado corta", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    var registrationDate = SimpleDateFormat("dd/MM/yyyy").format(Date())
                    var db = FirebaseFirestore.getInstance()
                    db.collection("users").document(email).set(hashMapOf(
                        "email" to email,
                        "user" to user,
                        "registrationDate" to registrationDate
                    ))
                    Toast.makeText(this, "Tu registro se ha completado satisfactoriamente", Toast.LENGTH_SHORT).show()
                    goHome(user, "email") // TODO inicio por mail, no necesario
                }
                else{
                    if("FirebaseAuthUserCollisionException" in it.exception.toString()){
                        Toast.makeText(this,"El email ya está registrado", Toast.LENGTH_SHORT).show()
                    }
                    else Toast.makeText(this,"Error en el registro del usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Move to view: home
    private fun goHome(username: String, provider: String){
        user = username
        providerSession = provider

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Move to view: login
    private fun goLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


}