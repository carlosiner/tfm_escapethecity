package uoc.tfm.escapethecity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ERInformationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_er_information)

        var db = FirebaseFirestore.getInstance()
//        db.collection("users").document("test").set(hashMapOf(
//            "email" to "myemail",
//            "user" to "myUser",
//            "registrationDate" to "01"
//        ))
        var textView: TextView = findViewById(R.id.tv_er_description)
        var escape:Escape
        db.collection("escapes").document("el_secreto").get()
            .addOnSuccessListener {
                println("----------------")
                println(it.data)
                escape = it.toObject(Escape::class.java)
                println("----------------")
            }
            .addOnFailureListener{
                println("Error in retrieving: " + it)
            }

//            .get()
//            .addOnSuccessListener{result ->
//                for (document in result){
//                    println("DOCS: ")
//                    println("${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener{exception ->
//                println(exception)
//

        // Menus: toolbar and navigation bar (top and lateral bars)
        var topBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.top_bar)
        topBar.title = "Juego: El Secreto" //TODO cambiar por variable BBDD
        drawerL = topBarActivation()
        lateralBarActivation(this)
    }

    // Override back
    override fun onBackPressed() {
        goBack()
    }


    // Actions selector
    fun er_actions(view: View){
        when(view.tag){
            "goBack" -> goBack()
        }
    }

    private fun setInformationText(text: String){

    }


    // Common item selection for navigationMenu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Button selection
        when (item.itemId){
            R.id.lateralmenu_home -> goMain()
            R.id.lateralmenu_mygames -> goMyGames()
            R.id.lateralmenu_chat -> goChat()
            R.id.lateralmenu_logout_button -> logout()
        }
        // Lateral menu closure
        drawerL.closeDrawer(GravityCompat.START)
        return true
    }
}