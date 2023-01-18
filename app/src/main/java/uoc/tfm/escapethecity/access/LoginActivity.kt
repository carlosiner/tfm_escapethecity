package uoc.tfm.escapethecity.access

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import uoc.tfm.escapethecity.BaseActivity
import uoc.tfm.escapethecity.MainActivity
import uoc.tfm.escapethecity.R

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

        var forgotPass: TextView = findViewById(R.id.etForgotPass)
        surlineText(forgotPass, getString(R.string.access_button_remember))
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