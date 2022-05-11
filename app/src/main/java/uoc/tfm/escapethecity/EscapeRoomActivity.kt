package uoc.tfm.escapethecity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

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
            "t_er_ranking" -> goERRanking()
        }
    }

    private fun goERInformation(){
        var intent = Intent(this, ERInformationActivity::class.java)
        startActivity(intent)
    }

    private fun goERAchievements(){
        var intent = Intent(this, ERAchievementsActivity::class.java)
        startActivity(intent)
    }

    private fun goERRanking(){
        var intent = Intent(this, ERRanking::class.java)
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