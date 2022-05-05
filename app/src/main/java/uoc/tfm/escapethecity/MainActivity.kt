package uoc.tfm.escapethecity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.DynamicLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.GridLayout
import android.widget.RelativeLayout
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
        layoutGenerator()

    }

    fun testingButton(view: View){
//        var relaLay: RelativeLayout = findViewById(R.id.rlDummyEscapeRoom)
//        relaLay.setOnClickListener{
//        }
        goEscapeRoom()
    }

    fun layoutGenerator(){
        for (x in 1..10){
            val rlDynamic: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            rlDynamic.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

    //        val tv_dynamic = TextView(this)
//            tv_dynamic.textSize = 20f
//            tv_dynamic.text = "This is a dynamic TextView generated programmatically in Kotlin"

            var gl: GridLayout = findViewById(R.id.gl_dinamic)
//            gl.addView(rlDynamic)
        }
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
    }

    private fun goMyGames(){
        // TODO
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    private fun goChat(){
        var intent = Intent(this,ChatActivity::class.java)
        startActivity(intent)
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        var intent = Intent(this,RegistrationActivity::class.java)
        startActivity(intent)
    }

    private fun goEscapeRoom(){
        var intent = Intent(this,EscapeRoomActivity::class.java)
        startActivity(intent)
    }


}