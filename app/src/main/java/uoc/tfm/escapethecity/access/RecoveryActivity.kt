package uoc.tfm.escapethecity.access

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import uoc.tfm.escapethecity.BaseActivity
import uoc.tfm.escapethecity.R

class RecoveryActivity : BaseActivity() {

    // Initialization
    private lateinit var etEmail: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery)

        auth = FirebaseAuth.getInstance()

        // Objects in view
        etEmail = findViewById(R.id.etEmail)

        var accessTV: TextView = findViewById(R.id.tvAccess)
        surlineText(accessTV, getString(R.string.recovery_button_access))
    }

    override fun onBackPressed() {
        finish()
    }

    //
    fun recovery_pass(view: View){
        recovery()
    }

    fun access(view: View){
        goAccess()
    }

    /* Private functions */

    private fun recovery(){
        var email = etEmail.text.toString()
        if (email.isNotEmpty()){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener{
                    if (it.isSuccessful) goAccess()
                    else{
                        // Error in password reset
                        Toast.makeText(this,
                            "Se ha producido un error en la recuperación de la contraseña",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun goAccess() {
        onBackPressed()
    }

}