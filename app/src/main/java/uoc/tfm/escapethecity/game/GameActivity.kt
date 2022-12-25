package uoc.tfm.escapethecity.game

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import uoc.tfm.escapethecity.BaseActivity
import uoc.tfm.escapethecity.R

class GameActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        loadTopBar()

        // Timer
        if (currentERUser.timer_activated){
            setTimerConfig()
        }


        // Checks if GPS is enabled
        if (!checkLocationEnabled()){
            // Generates a Dialog box to activate the GPS
            requestGPSActivation()
        }
        // Gets the permissions needed
        initPermissionsGPS()
//        getLocation()
    }

    private fun setTimerConfig() {
        /* Set time config if selected */
        val rlTimer: RelativeLayout = findViewById(R.id.rl_g_timer)
        timerSetEndDate()
        rlTimer.isClickable = true
        rlTimer.visibility = View.VISIBLE


    }

    override fun onResume() {
        super.onResume()
//        getLocation()
    }


    // Actions selector
    fun selectActions(view: View){
        when(view.tag){
            "rl_g_investigation" -> goInvestigation()
            "rl_g_map" -> goMap()
            "rl_g_inventory" -> goInventory()
            "rl_g_logbook" -> goLogbook()
            "rl_g_options" -> goOptions()
            "rl_g_timer" -> goTimer()
        }
    }
    private fun goInvestigation() {
        var intent = Intent(this, GInvestigationActivity::class.java)
        startActivity(intent)
    }

    private fun goMap() {
        var intent = Intent(this, GMapActivity::class.java)
        startActivity(intent)
    }

    private fun goInventory() {
        var intent = Intent(this, GInventoryActivity::class.java)
        startActivity(intent)
    }

    private fun goLogbook() {
        var intent = Intent(this, GLogbookActivity::class.java)
        startActivity(intent)
    }

    private fun goOptions() {
        var intent = Intent(this, GOptionsActivity::class.java)
        startActivity(intent)
    }

    private fun goTimer() {
        var intent = Intent(this, GTimerActivity::class.java)
        startActivity(intent)
    }


    /* --------------- COMMON --------------- */

    // Override back
    override fun onBackPressed() {
        goEscapeRoom()
    }

    // Common item selection for navigationMenu
    private fun loadTopBar() {
        var topBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.top_bar)
        topBar.title = "Juego: " + currentERContent.name
        drawerL = topBarActivation()
        lateralBarActivation(this)
    }

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