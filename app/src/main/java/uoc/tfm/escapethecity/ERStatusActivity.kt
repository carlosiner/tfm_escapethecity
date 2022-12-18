package uoc.tfm.escapethecity


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
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
        val tGame: TextView = findViewById(R.id.tv_er_status_game_content)
        val tDate: TextView = findViewById(R.id.tv_er_status_date_content)
        val tTime: TextView = findViewById(R.id.tv_er_status_time_content)
        val bStart: Button = findViewById(R.id.b_er_status_start)
        var startTime = currentERUser.user_date_selected
        val startParam = LocalDateTime.ofInstant(Instant.ofEpochSecond(startTime),
            TimeZone.getDefault().toZoneId())

        tGame.text = currentERContent.name
        tDate.text = startParam.toLocalDate().toString()
        tTime.text = startParam.toLocalTime().toString()

        if (flagStart){
            tTitle.text = "¡Ya puedes comenzar tu aventura!"
            tSubTitle.text = "¿Estás listo? \n "+
                    "Diríjete a la siguiente dirección y pulsa el botón de empezar"
            bStart.visibility = View.VISIBLE
        }
        else{
            tTitle.text = "¡La sesión ha sido creada!"
            tSubTitle.text = "Tu desafío comenzará pronto"
            bStart.visibility = View.INVISIBLE
        }
    }

    private fun checkCancel(){
        // TODO PopUp
        goCancel()
    }

    // Actions selector
    fun selectActions(view: View){
        // Select a destination function based on the tag
        when(view.tag){
            "goBack" -> goBack()
            "b_er_status_start" -> goGame()
            "b_er_status_cancel" -> checkCancel()
        }
    }

    private fun goGame(){
        // Update the user current information to the final state
        currentERUser.user_status = 3
        // Update db
        updateUserEscapeRoom()
        // Go to Game
        var intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
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