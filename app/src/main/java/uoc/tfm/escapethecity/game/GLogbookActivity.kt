package uoc.tfm.escapethecity.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import uoc.tfm.escapethecity.BaseActivity
import uoc.tfm.escapethecity.R
import uoc.tfm.escapethecity.data.GameItems
import uoc.tfm.escapethecity.data.GameUserLogs

class GLogbookActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener  {

    // Common Drawer definition
    private lateinit var drawerL: DrawerLayout
    private lateinit var items: ArrayList<GameUserLogs>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glogbook)

        loadTopBar()
        getItemsList()
    }

    private fun getItemsList(){
        val recyclerview: RecyclerView = findViewById(R.id.recyclerviewLogbook)

        // Manager of the view, in this case with an vertical LinearLayout
        recyclerview.layoutManager = LinearLayoutManager(this)
        items = ArrayList()

        // Get items from scape room list
        for (i in currentERUser.user_logs){
            items.add(i.value)
        }
        // Sort by Date
        items.sortByDescending { it.log_time }

        // This will pass the ArrayList to the Adapter
        val adapter = GLogbookAdapter(items)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
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