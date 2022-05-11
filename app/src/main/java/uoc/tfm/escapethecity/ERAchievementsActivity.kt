package uoc.tfm.escapethecity

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
import uoc.tfm.escapethecity.CustomRVAdapter
import uoc.tfm.escapethecity.R
import uoc.tfm.escapethecity.data.ItemsViewModel

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

        generateAchievementsList()
    }

    private fun generateAchievementsList(){
        val recyclerview: RecyclerView = findViewById(R.id.recyclerview)

        // Manager of the view, in this case with an horizontal LinearLayout
        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<ItemsViewModel>()

        /* TODO:
            First time: Load from DB the default list of items with descriptions,
                    - Use the "trophy" image
                    - The items are blocked for the user
            Next times: Load from the DB the status of this user related with the escape room game
                    - Custom images
         */

        // Dummy array to load default data TODO: replace it with DB
        data.add(ItemsViewModel("t_a1", R.drawable.trophy, "Rápido"))
        data.add(ItemsViewModel("t_a2", R.drawable.trophy, "Llave dorada"))
        data.add(ItemsViewModel("t_a3", R.drawable.trophy, "Final inesperado"))

        // This will pass the ArrayList to our Adapter
        val adapter = CustomRVAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
    }

    // Override back
    override fun onBackPressed() {
        goBack()
    }


    // Actions selector
    fun er_actions(view: View){
        // TODO change this with the "data" itemList information

        when(view.tag){
            "goBack" -> goBack()
            "t_a1" -> Toast.makeText(this,"No has conseguido este logro", Toast.LENGTH_SHORT).show()
            "t_a2" -> Toast.makeText(this,"No has conseguido este logro", Toast.LENGTH_SHORT).show()
            "t_a3" -> Toast.makeText(this,"No has conseguido este logro", Toast.LENGTH_SHORT).show()

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