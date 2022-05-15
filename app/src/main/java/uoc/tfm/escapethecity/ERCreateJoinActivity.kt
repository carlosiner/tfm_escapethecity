package uoc.tfm.escapethecity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ERCreateJoinActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_er_create_join)
    }


    // Override back
    override fun onBackPressed() {
        goBack()
    }


    // Actions selector
    fun er_actions(view: View){
        when(view.tag){
            "goBack" -> goBack()
            "b_er_groups" -> goGroups()
            "b_er_time_yes" -> setTimeSelector(true, true)
            "b_er_time_no" -> setTimeSelector(false, true)
            "b_er_date_selection" -> setDate()
            "b_er_confirmation" -> confirm()
        }
    }

    private fun goGroups(){
        /* TODO
            Group Activity to join or create a new group
         */

        // TODO: modify, dummy solution to show functionality
        val tvNameGroup: TextView = findViewById(R.id.tv_group_name)
        tvNameGroup.text = "My test group"
        setTimeSelector(false, false)
    }

    private fun setTimeSelector(selection:Boolean, clickable:Boolean){
        val buttonYes: Button = findViewById(R.id.b_er_time_yes)
        val buttonNo: Button = findViewById(R.id.b_er_time_no)
        if(selection){
            buttonYes.backgroundTintList = ContextCompat.getColorStateList(this,R.color.escape_vivid_purple)
            buttonYes.alpha = 1f
            buttonNo.backgroundTintList = ContextCompat.getColorStateList(this,R.color.escape_dead_purple)
            buttonNo.alpha = 0.25f
        }
        else{
            buttonNo.backgroundTintList = ContextCompat.getColorStateList(this,R.color.escape_vivid_purple)
            buttonNo.alpha = 1f
            buttonYes.backgroundTintList = ContextCompat.getColorStateList(this,R.color.escape_dead_purple)
            buttonYes.alpha = 0.25f
        }
        buttonYes.isClickable = clickable
        buttonNo.isClickable = clickable

    }

    private fun setDate(){
        // TODO
    }

    private fun confirm(){
        // TODO
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