package uoc.tfm.escapethecity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
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

        load_db_escapes()

    }

    fun testingButton(view: View){
//        var relaLay: RelativeLayout = findViewById(R.id.rlDummyEscapeRoom)
//        relaLay.setOnClickListener{
//        }
        goEscapeRoom()
    }

    private fun load_db_escapes(){
        val db = FirebaseFirestore.getInstance()

        db.collection("escapes")
            .get()
            .addOnSuccessListener {
                for (sr in it.documents){
                    var key: String = sr.id
                    var escapeObj: Escape = sr.toObject(Escape::class.java)!! // Content of SR
                    escapeList[key] = escapeObj
                }
                Log.d("Contenido encontrado", "Hay contenido ")
                generateEscapeList()
            }
            .addOnFailureListener {
                Log.d("Error loading escapes", "get failed with ", it)
            }
        Log.d("Contenido encontrado", "Hay contenido ")

//        db.collection("escapes").document("er1")
//            .get()
//            .addOnSuccessListener {
//                if (it.data?.size != null){
//                    // it.get("achievements")
//                    //Log.println(Log.INFO,"DONE","Done")
//                    var escapeObj: Escape = it.toObject(Escape::class.java)!!
//                    escapeList.add(escapeObj)
//                }
//                else{
////                   val dbUp: FirebaseFirestore = FirebaseFirestore.getInstance()
////                   dbUp.collection("destino").document("usermail").set(hashMapOf(
////                       // set default values
////                        "my_field" to 0.0, ...
////                   ))
//                    Log.d("Error in DB content", "Error when loading escapes list")
//                }
//            }
//            .addOnFailureListener {
//                Log.d("Error loading escapes", "get failed with ", it)
//            }
    }

    private fun generateEscapeList(){
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

//    fun layoutGenerator(){
//        for (x in 1..10){
//            val rlDynamic: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
//            rlDynamic.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//
//    //        val tv_dynamic = TextView(this)
////            tv_dynamic.textSize = 20f
////            tv_dynamic.text = "This is a dynamic TextView generated programmatically in Kotlin"
//
//            var gl: GridLayout = findViewById(R.id.gl_dinamic)
////            gl.addView(rlDynamic)
//        }
//    }


    private fun goEscapeRoom(){
        // TODO
        // Define the escape room id when enter in one
        // actualSR must get a value
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