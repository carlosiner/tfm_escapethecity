package uoc.tfm.escapethecity.escaperoom


import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import uoc.tfm.escapethecity.BaseActivity
import uoc.tfm.escapethecity.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class ERStatusActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Common Drawer definition
    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_erstatus)
        // Common top bar load
        loadTopBar()
        setStatusInformation(checkERStart())
    }

    private fun setStatusInformation(flagStart: Boolean){
        // Modifies the information from the Status
        val tTitle: TextView = findViewById(R.id.tv_er_status_title_message)
        val tSubTitle: TextView = findViewById(R.id.tv_er_status_subtitle_message)
        val tLocation: TextView = findViewById(R.id.tv_er_status_check_maps)
        val tGame: TextView = findViewById(R.id.tv_er_status_game_content)
        val tDate: TextView = findViewById(R.id.tv_er_status_date_content)
        val tTime: TextView = findViewById(R.id.tv_er_status_time_content)
        val bStart: Button = findViewById(R.id.b_er_status_start)
        var startTime = currentERUser.user_date_selected
        val startParam = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime),
            TimeZone.getDefault().toZoneId())

        surlineText(tLocation, getString(R.string.tv_er_status_check_maps))

        tGame.text = currentERContent.name
        tDate.text = startParam.toLocalDate().toString()
        tTime.text = startParam.toLocalTime().toString()

        if (flagStart){
            tTitle.text = getString(R.string.tv_er_status_description_after_start_title)
            tSubTitle.text = getString(R.string.tv_er_status_description_after_start_subtitle)
            bStart.visibility = View.VISIBLE
        }
        else{
            tTitle.text = getString(R.string.tv_er_status_description_before_start_title)
            tSubTitle.text = getString(R.string.tv_er_status_description_before_start_subtitle)
            bStart.visibility = View.INVISIBLE
        }
    }

    private fun checkCancel(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.tv_er_status_cancel_game_title))
            .setMessage(getString(R.string.tv_er_status_cancel_game_description))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.tv_game_options_confirmation_yes),
                DialogInterface.OnClickListener{ _, _ ->
                    goCancel()
                })
            .setNegativeButton(getString(R.string.tv_game_options_confirmation_no),
                DialogInterface.OnClickListener{ _, _ ->
                    // Do nothing
                })
            .show()

    }

    // Actions selector
    fun selectActions(view: View){
        // Select a destination function based on the tag
        when(view.tag){
            "goBack" -> goBack()
            "b_er_status_start" -> goGameAndUpdate()
            "b_er_status_cancel" -> checkCancel()
            "tv_er_status_check_maps" -> goMaps()
        }
    }

    private fun goMaps() {
        /* Open Google maps to the initial location */
        var erPosition = currentERContent.location
        // Set the position and the marker
        val gmmIntentUri = Uri.parse("geo:0,0?q=" +
                "${erPosition!!.latitude},${erPosition!!.longitude}("
                +getString(R.string.tv_er_status_check_maps_name)+")")
        // Itemn to the Uri defined for Maps
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        // Use the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun goGameAndUpdate(){
        // Update the user current information to the final state
        currentERUser.user_status = 3

        // Log event
        setUserLog(
            getString(R.string.tv_game_userlog_title_SER),
            getString(R.string.tv_game_userlog_desc_SER)
        )

        // if there is an initial item, get it
        var idInitialItem = currentERContent.initial_item
        if (idInitialItem != ""){
            getSoleItem(idInitialItem!!)
        }
        // Update db
        updateUserEscapeRoom()
        // Go to Game
        goGame()
    }

    private fun goCancel(){
        // Cancel the ER
        // User status changed to 0
        currentERUser.user_status = 0
        // Save the config in the DB
        updateUserEscapeRoom()
        // Return to the ER view
        goBack()
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