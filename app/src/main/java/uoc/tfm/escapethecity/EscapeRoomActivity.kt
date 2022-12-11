package uoc.tfm.escapethecity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import uoc.tfm.escapethecity.data.UserEscape

class EscapeRoomActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escape_room)

        load_db_escaperoom()
    }

    override fun onResume() {
        super.onResume()
        change_status()
    }

    private fun first_er_load(){
        /* First time that a user accesses a escape room */
        // Clean instance of currentERUser
        currentERUser = UserEscape()
        currentERUser.let {
            // Copy parameters from the escape room, rest by default
            it.id = currentERContent.id
            it.achievements = currentERContent.achievements
            it.items = currentERContent.items
            it.zones = currentERContent.zones
            it.trials = currentERContent.trials
        }
    }


    private fun load_db_escaperoom() {
        val db = FirebaseFirestore.getInstance()
        // Retrieves information of the user escape room from the global object
        // TODO Check first if escape load in
        if(!currentERId.isEmpty()
            || currentERContent.id != currentERId){
            // Reload escape info
            try {
                db.collection("escapes_by_users").document(userInfo.email!!)
                    .collection("escapes").document(currentERId).get()
                    .addOnSuccessListener {
                        if (it.data?.size != null){
                            // Load a previous configuration
                            currentERContent = escapeList[currentERId]!!
                            currentERUser = it.toObject(UserEscape::class.java)!!
                            loadTopBar()
                            change_status()

                        }
                        else{
                            // Generate new entry of the escape room to the user db
                            currentERContent = escapeList[currentERId]!!
                            first_er_load()
                            db.collection("escapes_by_users")
                                .document(userInfo.email!!).collection("escapes")
                                .document(currentERId).set(currentERUser)
                            loadTopBar()
                            change_status()
                        }
                    }
                    .addOnFailureListener {
                        Log.d("Error loading escapes", "get failed with ", it)
                    }
            }
            catch (err: Exception){
                Log.d("error-db", "Cannot access to the DB to load the escape room")
            }
        } else{
            Log.d("error-escape", "There is no information about the escape room id")
        }
    }

    private fun change_status(){
        /* Changes the status of the ER view */
        var image:ImageView = findViewById(R.id.im_er_multirole)
        var text: TextView = findViewById(R.id.tx_er_multirole)

        if (currentERUser.user_status == 1){
            image.setImageResource(R.drawable.icon_game_review_state)
            text.text = "Ver Estado"
        }
        else if (currentERUser.user_status == 2){
            image.setImageResource(R.drawable.icon_game_start)
            text.text = "Empezar"
        }
        else if (currentERUser.user_status == 3){
            image.setImageResource(R.drawable.icon_g_continue)
            text.text = "Continuar"
        }
        else{
            image.setImageResource(R.drawable.icon_g_create_or_join)
            text.text = "Crear Partida"
        }
    }

    private fun selectMulti(){
        // Change the MultiButton selection given the status of the ER
        if (currentERUser.user_status == 0){
            // User to create ER
            goERCreate()
        }
        else if (currentERUser.user_status == 1
            || currentERUser.user_status == 2){
            // User to check status or to start
            goERStatus()
        }
        else if (currentERUser.user_status == 3){
            // Continue ER game
            goGame()
        }
        else{
            Log.d("Error loading escapes", "Error when checking currentERUser status ")
        }
    }

    fun er_selection(view:View){
        when(view.tag){
            "t_er_achievements" -> goERAchievements()
            "t_er_info" -> goERInformation()
            "t_er_ranking" -> goERRanking()
            "t_er_multirole" -> selectMulti()
        }
    }

    private fun goERInformation(){
        var intent = Intent(this, ERInformationActivity::class.java)
        startActivity(intent)
    }

    private fun goERAchievements(){
        var intent = Intent(this, ERAchievementsActivity::class.java)
        startActivity(intent)
    }

    private fun goERRanking(){
        var intent = Intent(this, ERRanking::class.java)
        startActivity(intent)
    }

    private fun goERCreate(){
        var intent = Intent(this, ERCreateActivity::class.java)
        startActivity(intent)
    }

    private fun goERStatus(){
        var intent = Intent(this, ERStatusActivity::class.java)
        startActivity(intent)
    }

    private fun goGame(){
        TODO()
//        var intent = Intent(this, GameActivity::class.java)
//        startActivity(intent)
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
            R.id.lateralmenu_chat -> goChat()
            R.id.lateralmenu_logout_button -> logout()
        }
        // Lateral menu closure
        drawerL.closeDrawer(GravityCompat.START)
        return true
    }
}