package uoc.tfm.escapethecity.game

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

class GInventoryActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    // Common Drawer definition
    private lateinit var drawerL: DrawerLayout
    private lateinit var items: ArrayList<GameItems>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ginventory)

        loadTopBar()
        getItemsList()
    }

    private fun getItemsList(){
        val recyclerview: RecyclerView = findViewById(R.id.recyclerviewInventory)

        // Manager of the view, in this case with an vertical LinearLayout
        recyclerview.layoutManager = LinearLayoutManager(this)
        items = ArrayList()

        // Get items from scape room list
        for (i in currentERUser.items){
            if (i.value.i_found && !i.value.i_used){
                items.add(i.value)
            }
        }

        // This will pass the ArrayList to the Adapter
        val adapter = GInventoryAdapter(items)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
    }




    fun selectActions(view: View) {
        if (view.tag == "goBack") {
            goBack()
        }
        else {
            var itemInfo: GameItems = currentERUser.items[view.tag]!!
            if (itemInfo.i_found) {
                createDialogItem(itemInfo.i_name!!, itemInfo.i_description!!)
            }
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