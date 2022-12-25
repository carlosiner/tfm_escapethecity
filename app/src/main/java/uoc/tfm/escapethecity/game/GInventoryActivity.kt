package uoc.tfm.escapethecity.game

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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


    private fun useItem(itemId: String){
        /* Use item in trial */
        if(currentGameTrialValue.t_id_item_used != ""){
            if (currentGameTrialValue.t_id_item_used == itemId &&
                currentERUser.items[itemId]!!.i_found){
                if (!currentERUser.items[itemId]!!.i_used) {
                    // Use the item
                    var itemName = currentERUser.items[itemId]!!.i_name
                    // Change objet status
                    currentERUser.items[itemId]!!.i_used = true

                    // Send notification (Toast)
                    Toast.makeText(
                        this,
                    getString(R.string.toast_game_item_used) + itemName,
                        Toast.LENGTH_SHORT
                    ).show()

                    // Log event
                    setUserLog(
                        getString(R.string.log_game_item_use_title) + itemName,
                        getString(R.string.log_game_item_use_desc),
                        20)
                }
                else{
                    // Item already used
                    Toast.makeText(
                        this,
                        getString(R.string.b_game_item_use_already),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else{
                // Not correct item for trial
                // Send notification (Toast)
                Toast.makeText(
                    this,
                    getString(R.string.b_game_item_use_not_correct_for_trial),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        else{
            // Trial no requiere esto
            // Send notification (Toast)
            Toast.makeText(
                this,
                getString(R.string.b_game_item_use_trial_not_require),
                Toast.LENGTH_SHORT
            ).show()

        }
    }



    fun selectActions(view: View) {
        if (view.tag == "goBack") {
            goBack()
        }
        else {
            var itemInfo: GameItems = currentERUser.items[view.tag]!!
            if (itemInfo.i_found) {
                createDialogItemInvetory(itemInfo.i_name!!, itemInfo.i_description!!,
                    view.tag as String
                )
            }
        }
    }

    private fun createDialogItemInvetory(title: String, message: String, itemId: String){
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton(getString(R.string.b_game_item_use),
                DialogInterface.OnClickListener{ _, _ ->
                    useItem(itemId)
                })
            .show()
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