package uoc.tfm.escapethecity

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    private fun checkCancel(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.tv_game_options_cancel))
            .setMessage(getString(R.string.tv_game_options_confirmation_message))
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
            "ib_goBack" -> goBack()
            "tv_g_options_cancel" -> checkCancel()
        }
    }

    private fun goCancel(){
        // Cancel the ER
        // User status changed to 0
        clearGameUserData()

        // Save the config in the DB
        updateUserEscapeRoom()
        // Return to the ER view
        goMain()
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