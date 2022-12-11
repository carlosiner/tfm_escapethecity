package uoc.tfm.escapethecity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import uoc.tfm.escapethecity.data.Escape
import uoc.tfm.escapethecity.data.ItemsViewModel

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Global vars
    private lateinit var drawerL: DrawerLayout
    private lateinit var data: ArrayList<Escape>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      //  layoutGenerator()

        drawerL = topBarActivation()
        lateralBarActivation(this)

        // TODO Change this
        //  To get the information from user collection!
        //  If there is no user info, then copy the structure from
        //  * The escapes collection
        //  into the user

        load_db_escapes()

    }

    private fun load_db_escapes(){
        val db = FirebaseFirestore.getInstance()

        // Gets the scape collection from the DB
        db.collection("escapes")
            .get()
            .addOnSuccessListener {
                for (sr in it.documents){
                    var key: String = sr.id
                    var escapeObj: Escape = sr.toObject(Escape::class.java)!! // Content of SR
                    escapeList[key] = escapeObj
                }
                generateEscapeList()
            }
            .addOnFailureListener {
                Log.d("Error loading escapes", "get failed with ", it)
            }
    }

    private fun generateEscapeList(){
        // Generate the list view of the escapes collection using a recyclerViews
        val recyclerView: RecyclerView = findViewById(R.id.rv_escaperoom)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        data = ArrayList()
        if (escapeList!= null){
            for ((key, obj) in escapeList){
                //var image = R.drawable.game_el_secreto // TODO Set a dummy image (or get the image)

                data.add(obj)
            }
        }

        val adapter = MainAdapter(data)
        recyclerView.adapter = adapter
    }

    fun e_list_actions(view: View){
        for (i in data){
            if (i.id == view.tag){
                if (i.enabled==true) {
                    currentERId = i.id!!
                    goEscapeRoom()
                }
                else{
                    // ER not enabled, do nothing
                    Toast.makeText(this,"Escape room no disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goEscapeRoom(){
        var intent = Intent(this,EscapeRoomActivity::class.java)
        startActivity(intent)
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