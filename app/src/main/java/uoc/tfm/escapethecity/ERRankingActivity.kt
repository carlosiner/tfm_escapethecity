package uoc.tfm.escapethecity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ERRankingActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_er_ranking)

        // Menus: toolbar and navigation bar (top and lateral bars)
        var topBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.top_bar)
        topBar.title = "Juego: El Secreto" //TODO change by DDBB variable
        drawerL = topBarActivation()
        lateralBarActivation(this)

        /* TODO
            - Get information from the database and order the points result
            - Get the information of the user
            - Show the information in the table
        */
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

    // Common item selection for navigationMenu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Button selection
        when (item.itemId){
            R.id.lateralmenu_home -> goMain()
            R.id.lateralmenu_mygames -> goMyGames()
            R.id.lateralmenu_logout_button -> logout()
        }
        // Lateral menu closure
        drawerL.closeDrawer(GravityCompat.START)
        return true
    }
}