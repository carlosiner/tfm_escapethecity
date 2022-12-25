package uoc.tfm.escapethecity.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import uoc.tfm.escapethecity.BaseActivity
import uoc.tfm.escapethecity.R
import java.time.LocalDateTime
import java.time.ZoneId

class GTimerActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gtimer)
        loadTopBar()
        setTimer()
        val clockImage: ImageView = findViewById(R.id.iv_game_timer_image)
        getImageFromURL(this,
            "https://firebasestorage.googleapis.com/v0/b/tfm-escapethecity.appspot.com/o/default_resources%2Fgame_timer.gif?alt=media&token=af541bb8-6f19-4332-9e5b-bd5bc48fc25b",
            clockImage, true)
    }

    fun setTimer(){
        val currentTime = getCurrentTimeInSeconds()
        val time = endTime - currentTime
        object : CountDownTimer(time*1000, 1000) {
            val tv_timer: TextView = findViewById(R.id.tv_game_timer)
            override fun onTick(millisUntilFinished: Long) {
                val hours = (millisUntilFinished / 1000) / 3600
                val minute = (millisUntilFinished / 1000) % 3600 / 60
                val seconds = (millisUntilFinished / 1000) % 60

                var strHour = if (hours<10) "0$hours" else hours.toString()
                var strMinute = if (minute<10) "0$minute" else minute.toString()
                var strSecond = if (seconds<10) "0$seconds" else seconds.toString()
                tv_timer.text = "$strHour:$strMinute:$strSecond"
            }

            override fun onFinish() {
                tv_timer.text = getString(R.string.tv_game_inventory_timer_finalized)
            }
        }.start()
    }



    // Actions selector
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
        topBar.title = "Juego: " + BaseActivity.currentERContent.name
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