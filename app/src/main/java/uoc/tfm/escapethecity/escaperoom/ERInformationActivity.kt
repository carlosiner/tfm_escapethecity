package uoc.tfm.escapethecity.escaperoom

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
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

        // Common top bar load
        loadTopBar()
        // Set content of view
        setViewInformation()
    }

    private fun setViewInformation(){
        val tvCity: TextView = findViewById(R.id.tv_er_information_city)
        val tvTime: TextView = findViewById(R.id.tv_er_information_time)
        val tvDifficulty: TextView = findViewById(R.id.tv_er_information_difficulty)
        val tvDescription: TextView = findViewById(R.id.tv_er_information_description)

        tvCity.text = currentERContent.city
        tvTime.text = (currentERContent.escape_duration!!/60).toString() +" " +
                getString(R.string.tv_er_information_time_unit)
        tvDifficulty.text = currentERContent.difficulty
        tvDescription.text = currentERContent.info
    }



    fun selectActions(view: View){
        // Select a destination function based on the tag
        when(view.tag){
            "goBack" -> goBack()
        }
    }

    /* --------------- COMMON --------------- */

    // Override back
    override fun onBackPressed() {
        goBack()
    }


    // Common item selection for navigationMenu
    private fun loadTopBar() {
        var topBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.top_bar)
        topBar.title = "Juego: " + currentERContent.name
        drawerL = topBarActivation()
        lateralBarActivation(this)
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