package uoc.tfm.escapethecity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class GOptionsActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goptions)
        loadTopBar()

        var cancelGame: TextView = findViewById(R.id.tv_g_options_cancel)
        surlineText(cancelGame, getString(R.string.tv_game_options_cancel))
    }

    private fun surlineText(tView: TextView, text: String){
        val sString = SpannableString(text)
        sString.setSpan(UnderlineSpan(), 0, sString.length, 0)
        tView.text = sString
    }

    private fun showPopUpWindowCancel(){
        // Disable background layout
        TODO()
//        var llBack = findViewById<LinearLayout>(R.id.ll_local_main)
//        llBack.isEnabled = false

    }

    private fun hidePopUpCancel(){
        TODO()
    }

    private fun checkCancel(){
        // TODO PopUp
        goCancel()
    }

    // Actions selector
    fun selectActions(view: View){
        // Select a destination function based on the tag
        when(view.tag){
            "ib_goBack" -> goBack()
            "tv_g_options_cancel" -> checkCancel()
        }
    }

    private fun goCancel(){
        // Cancel the ER
        // User status changed to 0
        currentERUser.user_status = 0
        // Save the config in the DB
        updateUserEscapeRoom()
        // Return to the ER view
        goEscapeRoom()
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