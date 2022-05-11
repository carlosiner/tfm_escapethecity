package uoc.tfm.escapethecity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import java.util.*

class EscapeRoomActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escape_room)

        // Menus: toolbar and navigation bar (top and lateral bars)
        var topBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.top_bar)
        topBar.title = "Juego: El Secreto" //TODO cambiar por variable BBDD
        drawerL = topBarActivation()
        lateralBarActivation(this)
    }

    fun er_selection(view:View){
        when(view.tag){
            "t_er_achievements" -> goERAchievements()
            "t_er_info" -> goERInformation()

        }
    }

    private fun goERInformation(){
        var intent = Intent(this,ERInformationActivity::class.java)
        startActivity(intent)
    }

    private fun goERAchievements(){
        var intent = Intent(this,ERAchievementsActivity::class.java)
        startActivity(intent)
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