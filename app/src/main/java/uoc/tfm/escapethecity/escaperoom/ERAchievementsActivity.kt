package uoc.tfm.escapethecity.escaperoom

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import androidx.recyclerview.widget.LinearLayoutManager
import uoc.tfm.escapethecity.BaseActivity
import uoc.tfm.escapethecity.R
import uoc.tfm.escapethecity.data.ERAchievements
import uoc.tfm.escapethecity.data.ItemsViewModel


class ERAchievementsActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerL: DrawerLayout
    private lateinit var data: ArrayList<ItemsViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_er_achievements)

        // Menus: toolbar and navigation bar (top and lateral bars)
        loadTopBar()

        generateAchievementsList()
    }

    private fun generateAchievementsList(){
        val recyclerview: RecyclerView = findViewById(R.id.recyclerview)

        // Manager of the view, in this case with an horizontal LinearLayout
        recyclerview.layoutManager = LinearLayoutManager(this)

        data = ArrayList()
        // Get items from scape room list
        for (i in currentERUser.achievements){
            var image = ""
            if (i.value.ac_active){
                image = i.value.ac_image!!
            }
            data.add(ItemsViewModel(i.key, image, i.value.ac_name!!))
        }

        // This will pass the ArrayList to the Adapter
        val adapter = CustomRVAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
    }

    // Override back
    override fun onBackPressed() {
        goBack()
    }


    // Actions selector
    fun selectActions(view: View){
        if (view.tag == "goBack"){ goBack() }
        else{
            var achInfo: ERAchievements = currentERUser.achievements[view.tag]!!
            if (achInfo.ac_active){
                createDialogItem(achInfo.ac_name!!, achInfo.ac_description!!)
            }
            else{
                Toast.makeText(this, R.string.tv_er_achievemnts_not_achieved, Toast.LENGTH_SHORT).show()
            }
        }
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
