package uoc.tfm.escapethecity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import uoc.tfm.escapethecity.data.User
import java.text.SimpleDateFormat
import java.util.*


class RegistrationActivity : BaseActivity(){
    companion object{
//        var userObj: User? = null
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



    public override fun onStart() {
        super.onStart()
        /* Maintain the user session if present */
        val user = auth.currentUser
        if (user != null){
            getDBUser(user.email!!) // Not null
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


    private fun registerUser(){
        /* Register a new user and move to view: home */
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
                    // TODO TESTING
                    val registeredUser = auth.currentUser
                    userInfo.username = userStr
                    userInfo.registrationDate = SimpleDateFormat("dd/MM/yyyy").format(Date())
                    userInfo.email = emailStr
                    userInfo.role = "player"
                    userInfo.image = "https://firebasestorage.googleapis.com/v0/b/tfm-escapethecity.appspot.com/o/default_resources%2Fdefault_user.png?alt=media&token=849fbfa5-a3ab-4799-9912-8973baee32da"

//                    userObj = userInfo
                    setDBUser()

                    if (registeredUser != null) {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(userInfo.username)
                            .setPhotoUri((userInfo.image)!!.toUri())
                            .build()
                        registeredUser.updateProfile(profileUpdates)
                    }


                    Toast.makeText(this, "Tu registro se ha completado satisfactoriamente", Toast.LENGTH_SHORT).show()
//                    mailER = emailStr
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
        // Saves the user information in Firebase
        var db = FirebaseFirestore.getInstance()
        db.collection("users").document(userInfo.email!!).set(userInfo!!)
    }

    private fun getDBUser(email: String){
        var db = FirebaseFirestore.getInstance()
        db.collection("users").document(email).get()
            .addOnSuccessListener {
//                userObj = User(
//                    it.data?.get("username") as String,
//                    it.data?.get("registrationDate") as String,
//                    it.data?.get("email") as String,
//                    it.data?.get("role") as String)
                loadUser()
                //TODO
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