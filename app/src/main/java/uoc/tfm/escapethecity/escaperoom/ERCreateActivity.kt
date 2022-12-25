package uoc.tfm.escapethecity.escaperoom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle

import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.text.format.DateFormat
import uoc.tfm.escapethecity.BaseActivity
import uoc.tfm.escapethecity.R

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class ERCreateActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // Common Drawer definition
    private lateinit var drawerL: DrawerLayout

    // Date info
    var selDay = 0
    var selMonth: Int = 0
    var selYear: Int = 0
    var flagDate = false
    var flagTimer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_er_create)
        // Common top bar load
        loadTopBar()
    }


    private fun setTimeSelector(selection:Boolean, clickable:Boolean){
        val buttonYes: Button = findViewById(R.id.b_er_time_yes)
        val buttonNo: Button = findViewById(R.id.b_er_time_no)
        var timer = false
        if(selection){
            buttonYes.backgroundTintList = ContextCompat.getColorStateList(this,
                R.color.escape_vivid_purple
            )
            buttonYes.alpha = 1f
            buttonNo.backgroundTintList = ContextCompat.getColorStateList(this,
                R.color.escape_dead_purple
            )
            buttonNo.alpha = 0.25f
            timer = true
            flagTimer = true
        }
        else{
            buttonNo.backgroundTintList = ContextCompat.getColorStateList(this,
                R.color.escape_vivid_purple
            )
            buttonNo.alpha = 1f
            buttonYes.backgroundTintList = ContextCompat.getColorStateList(this,
                R.color.escape_dead_purple
            )
            buttonYes.alpha = 0.25f
            timer = false
            flagTimer = true
        }
        buttonYes.isClickable = clickable
        buttonNo.isClickable = clickable
        currentERUser.timer_activated = timer
    }

    private fun setDate(){
        // Enable listeners for Time selection
        val calendar: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        // Date listener (Calendar)
        val calendar: Calendar = Calendar.getInstance()
        selYear = year
        selMonth = month+1 // moths are from 0-11
        selDay = day

        val timePickerDialog = TimePickerDialog(this, this,
            calendar.get(Calendar.HOUR_OF_DAY), //24h format
            calendar.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(this))
        timePickerDialog.show()
    }


    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        // Time listener (Clock)

        // Adjust string dates
        var strMonth = if (selMonth<10) "0$selMonth" else selMonth.toString()
        var strDay = if (selDay<10) "0$selDay" else selDay.toString()
        var strHour = if (hour<10) "0$hour" else hour.toString()
        var strMinute = if (minute<10) "0$minute" else minute.toString()

        var erDate = "$selYear-$strMonth-$strDay"
        var erTime = "$strHour:$strMinute"
        var dateInstant = LocalDateTime.parse(erDate +"T"+erTime).atZone(ZoneId.systemDefault()).toInstant()

        // Show the time in the View
        var textView = findViewById<TextView>(R.id.tv_group_date)
        textView.text = erDate +" "+ erTime

        // Set User information
        currentERUser.user_date_selected = dateInstant.epochSecond

        var now = getCurrentTimeInSeconds()
        if (now <= dateInstant.epochSecond){
            flagDate = true
        }
    }


    private fun createGame(){
        // Changes status of the User Escape Room
        if (flagDate && flagTimer){
            currentERUser.user_status = 1 // Created
            // update DDBB
            updateUserEscapeRoom()
            // Close the current move to ERMain window
            goBack()
        }
        else{
            if(!flagDate){
                // Date not set
                Toast.makeText(this,
                    R.string.toast_er_multirole_not_date,
                    Toast.LENGTH_SHORT).show()
            }
            else{
                // Timer not set
                Toast.makeText(this,
                    R.string.toast_er_multirole_not_timer,
                    Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Actions selector
    fun selectActions(view: View){
        when(view.tag){
            "goBack" -> goBack()
            "b_er_date_selection" -> setDate()
            "b_er_time_yes" -> setTimeSelector(true, true)
            "b_er_time_no" -> setTimeSelector(false, true)
            "b_er_confirmation" -> createGame()
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