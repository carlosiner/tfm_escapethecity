package uoc.tfm.escapethecity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uoc.tfm.escapethecity.data.Escape
import uoc.tfm.escapethecity.data.User
import uoc.tfm.escapethecity.data.UserEscape

open class BaseActivity : AppCompatActivity()  {

    companion object {
        /* General information */
        var escapeList: HashMap<String, Escape> = hashMapOf()

        /* User specific info */
        var userInfo: User = User()

        /* Selected escape room  */
        var currentERId: String = ""
        var currentERContent: Escape = Escape()
        var currentERUser: UserEscape = UserEscape()
    }

    /* Private variables */
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var drawerL: DrawerLayout
    private lateinit var lateralView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    /*
     ---------------    Functions     ---------------
    */

    fun getImageFromURL(context: Context, imageULR: String, imageView: ImageView){
        Glide.with(context).load(imageULR).fitCenter().into(imageView)
    }



    fun loadUser(){
        /* Load user config from Firebase Auth */
        val authUser = auth.currentUser!!
        userInfo.username = authUser.displayName
        userInfo.email = authUser.email
        userInfo.image = authUser.photoUrl.toString()
    }

    fun updateUserEscapeRoom(){
        // Updates the User's escape room with the current info
        db.collection("escapes_by_users")
            .document(userInfo.email!!).collection("escapes")
            .document(currentERId).set(currentERUser)
    }

    /* --- Toolbar and Navigation bars */
    fun topBarActivation(): DrawerLayout {
        // Generate the variable from the top_bar layout
        var topBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.top_bar)
        setSupportActionBar(topBar)

        // Get the DL from the main activity
        drawerL = findViewById(R.id.mainDL)
        var toggle = ActionBarDrawerToggle(
            this,
            drawerL,
            topBar,
            R.string.lateralmenu_open,
            R.string.lateralmenu_close)



        drawerL.addDrawerListener(toggle)
        toggle.syncState()

        return drawerL
    }


    fun lateralBarActivation(navVListener: NavigationView.OnNavigationItemSelectedListener){
        var lateralBar: NavigationView = findViewById(R.id.lateralview)
        // Added a listener to allow actions in the lateralBar
        lateralBar.setNavigationItemSelectedListener(navVListener)
        // Allow to use the icon original colours
        lateralBar.itemIconTintList = null

        // Added Profile header to the lateral menu
        lateralView = LayoutInflater.from(this).inflate(
            R.layout.menu_profile,
            lateralBar,
            false)

        // Reloads the view for new user login
        lateralBar.removeHeaderView(lateralView)
        lateralBar.addHeaderView(lateralView)


//        if (!this::user.isInitialized || !this::email.isInitialized ){
        if (userInfo.username == null || userInfo.email == null){
//            if (RegistrationActivity.userObj != null){
////                user = RegistrationActivity.userObj!!.username
////                email = RegistrationActivity.userObj!!.email
//                user = userInfo.username!!
//                email = userInfo.email!!
//            }
//            else {
//                user = "Username"
//                email = "email@email"
//            }
            loadUser()
        }
        var tvUser: TextView = lateralView.findViewById(R.id.menu_profile_email)
        tvUser.text = userInfo.email
        tvUser = lateralView.findViewById(R.id.menu_profile_name)
        tvUser.text = userInfo.username
        val tvUserImage: ImageView = lateralView.findViewById(R.id.menu_profile_image)
        getImageFromURL(lateralView.context, userInfo.image!!, tvUserImage)
        //Glide.with(lateralView.context).load(userInfo.image).fitCenter().into(tvUserImage)
    }

    /* Functions from NavigationMenu (lateral menu) */
    fun goMain(){
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun goMyGames(){
        // TODO - It should redirect to a Main with the games from the user (defined in the DB)
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun goChat(){
        var intent = Intent(this,ChatActivity::class.java)
        startActivity(intent)
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
        var intent = Intent(this,RegistrationActivity::class.java)
        startActivity(intent)
    }

    /* Other functions */
    fun goBack(){
        finish()
    }

}