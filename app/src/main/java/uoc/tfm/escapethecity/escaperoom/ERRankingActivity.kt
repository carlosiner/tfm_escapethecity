package uoc.tfm.escapethecity.escaperoom

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import uoc.tfm.escapethecity.BaseActivity
import uoc.tfm.escapethecity.R
import uoc.tfm.escapethecity.data.UserRanking
import uoc.tfm.escapethecity.data.UserRankingPosition

class ERRankingActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerL: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_er_ranking)

        // Common top bar load
        loadTopBar()

        // Get Ranking information for this ER
        setViewRanking()

    }

    private fun setViewRanking() {
        var usersRanking = arrayListOf<UserRanking>()
        var usersRankAndPos = arrayListOf<UserRankingPosition>()

        val db = FirebaseFirestore.getInstance()

        try {
            db.collection("ranking").document(currentERId).get()
                .addOnSuccessListener {
                    if (it.data?.size != null) {
                        for (i in it.data!!){
                            var uR = UserRanking()
                            var mapValue = i.value as MutableMap<String, Any>
                            uR.user_name = mapValue["user_name"].toString()
                            uR.user_points = mapValue["user_points"].toString().toInt()
                            usersRanking.add(uR)
                        }
                    }
                    // Order
                    usersRanking.sortByDescending { it.user_points }

                    var pos = 1
                    for (i in usersRanking){
                        var uRP = UserRankingPosition()
                        uRP.user_position = pos
                        uRP.user_name = i.user_name
                        uRP.user_points = i.user_points
                        usersRankAndPos.add(uRP)
                        pos++
                    }

                    usersRankAndPos.sortBy { it.user_position }

                    setView(usersRankAndPos)
                }
        }
        catch (err: Exception){
            Log.d("error-db", "Cannot access to the DB to load the ranking info")
        }
    }

    private fun setView(usersRankAndPos: ArrayList<UserRankingPosition>){
        val recyclerview: RecyclerView = findViewById(R.id.recyclerviewRanking)

        // Manager of the view, in this case with an vertical LinearLayout
        recyclerview.layoutManager = LinearLayoutManager(this)

        // This will pass the ArrayList to the Adapter
        val adapter = ERRankingAdapter(usersRankAndPos)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        // Set user total points
        var userPoints: TextView = findViewById(R.id.tv_er_ranking_user_position)
        userPoints.text = currentERUser.user_points.toString()
    }


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
        topBar.title = "Juego: " + currentERContent.name
        drawerL = topBarActivation()
        lateralBarActivation(this)
    }

    // Common item selection for navigationMenu
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