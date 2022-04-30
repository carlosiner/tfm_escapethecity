package uoc.tfm.escapethecity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionProvider
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    companion object{
        lateinit var usermail: String
        lateinit var user: String
        lateinit var providerSession: String
    }

    // private var email by Delegates.notNull<String>()
    private var username by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    // private lateinit var etEmail: EditText
    private lateinit var etUser: EditText
    private lateinit var etMail: EditText // TODO change it with mail if is easier than user
    private lateinit var etPass: EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        etUser = findViewById(R.id.etUser)
//        etMail = findViewById(R.id.etMail)
        etPass = findViewById(R.id.etPass)
    }

    fun login(view: View){
        loginUser()
    }

    // Move to view: recovery
    fun forgotPass(view: View){
        goRecovery(etUser.toString())
    }

    //TODO to change it with mail
    private fun loginUser(){
        username = etUser.toString()
        password = etPass.toString()

        auth.signInWithEmailAndPassword(username,password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful) goHome(username, "email") // TODO inicio por mail, no necesario
                /*else{
                    if ()
                }*/
            }
    }

    // Move to view: home
    private fun goHome(username: String, provider: String){
        user = user
        providerSession = provider

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