package uoc.tfm.escapethecity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ERAchievementsActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_er_achievements)

        // Menus: toolbar and navigation bar (top and lateral bars)
        var topBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.top_bar)
        topBar.title = "Juego: El Secreto" //TODO change by DDBB variable
        drawerL = topBarActivation()
        lateralBarActivation(this)

        generateAchievements()
    }


    private fun generateAchievements(){

        val mainLL: LinearLayout = findViewById(R.id.ll_insertion)

//        for (i in 0..5){
            var x: View = LayoutInflater.from(this).inflate(R.layout.achievement, mainLL, false)
//            var img: ImageView = findViewById(R.id.iv_a_imageX)
//            img.setId(i)
//            var t: TextView = findViewById(R.id.tv_a_textX)
//            t.setId(i)
//            t.setText("Logro: ")
//        }
        mainLL.removeAllViews()
        mainLL.addView(x)

//        var imageAndText =
//        for (i in 0..5){
//            val newLL: LinearLayout = LinearLayout(this)
//            newLL.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT)
//
//            var newIV: ImageView = ImageView(this)
//            newIV.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT)
//
//            newIV.setImageResource(R.drawable.trophy)
//            newIV.layoutParams.height = 150
//            newIV.layoutParams.width = 150
//
//
//            newLL.addView(newIV)
//
//            //mainLL.removeAllViews()
//            var x = LayoutInflater.from(this).inflate(R.id.ll_insertion, null)
//
//            mainLL.addView(newLL)
//        }





    }

    // Override back
    override fun onBackPressed() {
        goBack()
    }


    // Actions selector
    fun er_actions(view: View){
        when(view.tag){
            "goBack" -> goBack()
        }
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