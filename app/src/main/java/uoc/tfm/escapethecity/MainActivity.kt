package uoc.tfm.escapethecity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Global vars
    private lateinit var drawerL: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        topBarActivation()
        lateralBarActivation()
    }

    private fun topBarActivation(){
        // Generate the variable from the top_bar layout
        var topBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.top_bar)
        setSupportActionBar(topBar)

        // Get the DL from the main activity
        drawerL = findViewById(R.id.dl_main)
        var toggle = ActionBarDrawerToggle(
            this,
            drawerL,
            topBar,
            R.string.lateralmenu_open,
            R.string.lateralmenu_close)

        drawerL.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun lateralBarActivation(){
        var lateralBar: NavigationView = findViewById(R.id.lateralview)
        // Added a listener to allow actions in the lateralBar
        lateralBar.setNavigationItemSelectedListener(this)
        // Allow to use the icon original colours
        lateralBar.itemIconTintList= null

        // Added Profile header to the lateral menu
        var lateralView: View = LayoutInflater.from(this).inflate(
            R.layout.menu_profile,
            lateralBar,
            false)

        // Reloads the view for new user login 
        lateralBar.removeHeaderView(lateralView)
        lateralBar.addHeaderView(lateralView)
        
        //
        var tvUser: TextView = lateralView.findViewById(R.id.menu_profile_email)
//        tvUser.text.//TODO selección de usuario/email
    }

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

    private fun goMain(){
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goMyGames(){
        // TODO
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goChat(){
        var intent = Intent(this,ChatActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        var intent = Intent(this,RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

}