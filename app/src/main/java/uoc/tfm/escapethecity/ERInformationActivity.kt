package uoc.tfm.escapethecity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import uoc.tfm.escapethecity.BaseActivity
import uoc.tfm.escapethecity.R

class ERInformationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_er_information)

        // Menus: toolbar and navigation bar (top and lateral bars)
        var topBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.top_bar)
        topBar.title = "Juego: El Secreto" //TODO change with DB varaible
        drawerL = topBarActivation()
        lateralBarActivation(this)
    }

    // Override back
    override fun onBackPressed() {
        goBack()
    }


    fun selection_actions(view: View){
        // Select a destination function based on the tag
        when(view.tag){
            "goBack" -> goBack()
        }
    }

    private fun setInformationText(text: String){
        // TODO set the text from the DDBB object
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