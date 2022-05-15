package uoc.tfm.escapethecity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uoc.tfm.escapethecity.data.User
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class RegistrationActivity : BaseActivity(){
    companion object{
        var userObj: User? = null
    }

    private lateinit var auth: FirebaseAuth

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
        val activeSession = auth.currentUser

        if (activeSession != null){
            getDBUser(activeSession.email!!) // Not null
            goHome()
        }
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
        val emailStr = etEmail.text.toString()
        val passStr = etPass.text.toString()
        val userStr = etUser.text.toString()

        // Check if there are values in the fields
        if (emailStr.isEmpty() || passStr.isEmpty() || userStr.isEmpty()){
            Toast.makeText(this,"Por favor, complete su usuario, email y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        // Check the password size
        if (passStr.length<6){
            Toast.makeText(this,"La contraseña es demasiado corta", Toast.LENGTH_SHORT).show()
            return
        }

        // Generation of new user
        auth.createUserWithEmailAndPassword(emailStr, passStr)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    var registrationDate = SimpleDateFormat("dd/MM/yyyy").format(Date())
                    userObj = User(userStr, registrationDate, emailStr, "player")
                    setDBUser()
                    Toast.makeText(this, "Tu registro se ha completado satisfactoriamente", Toast.LENGTH_SHORT).show()
                    goHome()
                }
                else{
                    if("FirebaseAuthUserCollisionException" in it.exception.toString()){
                        Toast.makeText(this,"El email ya está registrado", Toast.LENGTH_SHORT).show()
                    }
                    else Toast.makeText(this,"Error en el registro del usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setDBUser(){
        var db = FirebaseFirestore.getInstance()
        db.collection("users").document(userObj!!.email).set(userObj!!)
    }

    private fun getDBUser(email: String){
        var db = FirebaseFirestore.getInstance()
        db.collection("users").document(email).get()
            .addOnSuccessListener {
                userObj = User(
                    it.data?.get("username") as String,
                    it.data?.get("registrationDate") as String,
                    it.data?.get("email") as String,
                    it.data?.get("role") as String)
                loadUserInProfile()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Error en la obtención del usuario", Toast.LENGTH_SHORT).show()
            }
    }

    // Move to view: home
    private fun goHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Move to view: login
    private fun goLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


}