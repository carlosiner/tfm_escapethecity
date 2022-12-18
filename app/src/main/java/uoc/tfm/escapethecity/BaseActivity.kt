package uoc.tfm.escapethecity

import android.annotation.SuppressLint
//import android.content.Context
import android.content.DialogInterface
//import android.content.Intent
import android.content.pm.PackageManager
//import android.location.Location
import android.location.LocationManager
//import android.location.LocationRequest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.os.Looper
import android.provider.Settings
//import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uoc.tfm.escapethecity.data.Escape
import uoc.tfm.escapethecity.data.User
import uoc.tfm.escapethecity.data.UserEscape
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.jar.Manifest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit

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

        /* Location */
        lateinit var fLocationProviderClient: FusedLocationProviderClient

    }

    /* Private variables */
    // For Firebase ops
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // For the Notification bar
    private lateinit var drawerL: DrawerLayout
    private lateinit var lateralView: View

    // For location
    private lateinit var locationRequest: LocationRequest // Updates
    private lateinit var locationCallback: LocationCallback // New locations
    private var currentLocation: Location? = null // Current location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    /*
     ---------------    Functions     ---------------
    */

    /* --- Utilities ---*/
    fun getImageFromURL(context: Context, imageULR: String, imageView: ImageView){
        /* Get image from URL (String) and insert into a view*/
        Glide.with(context).load(imageULR).fitCenter().into(imageView)
    }


    fun closeKeyBoard(view: View) {
        /* Closes the current view open keyboard
        * Source from:
        * https://stackoverflow.com/questions/47907441/close-hide-softinputkeyboard-in-kotlin
        * */
        view.apply {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun createDialogItem(title: String, message: String, ){
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .show()
    }

    /* --- Time & Date --- */
    fun checkERStart(): Boolean {
        /* Checks if the Game can start, the user is in the right time */
        val currentTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
        val startTime = currentERUser.user_date_selected
        val minAdd = 60 * 30 // 30m difference
        return (currentTime + minAdd) >= startTime
    }


    /* --- Firestore operations --- */
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

    /* --- Location --- */
    fun checkLocationEnabled(): Boolean{
        /* Checks if the user has the GPS enabled */
        var lManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        var netEnabled = lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return (gpsEnabled || netEnabled)

    }

    fun requestGPSActivation(){
        // Generates a Dialog box to activate the GPS
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.tv_game_gps_dialog_title))
            .setMessage(getString(R.string.tv_game_gps_dialog_message))
            .setPositiveButton(getString(R.string.tv_game_gps_dialog_confirm),
                DialogInterface.OnClickListener{ dialog, which ->
                    activateGPS()
                })
            .setNegativeButton(getString(R.string.tv_game_gps_dialog_cancel),
                DialogInterface.OnClickListener{ dialog, which ->
                    Toast.makeText(this,"Por favor active el GPS", Toast.LENGTH_SHORT).show()
                })
            .show()
    }

    private fun activateGPS(){
        val int = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(int)
    }

    private fun checkGPSPermissions(): Boolean {
        /* Checks permissions */
        val pAccessCoLocation = ContextCompat.checkSelfPermission(
            baseContext,android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val pAccessFiLocation = ContextCompat.checkSelfPermission(
            baseContext,android.Manifest.permission.ACCESS_FINE_LOCATION)
        return (pAccessCoLocation == PackageManager.PERMISSION_GRANTED &&
                pAccessFiLocation == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestGPSPermissions(){
        ActivityCompat.requestPermissions(this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            42)
    }

    fun initPermissionsGPS(){
        /* Initialize the service location client or request location permissions */
        if(checkGPSPermissions()){
            // Initialize Location updates
            fLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        }
        else{
            requestGPSPermissions()
        }
    }

//    @SuppressLint("MissingPermission")
//    private fun requestLocation(){
//        /* Get user location (if all is enabled) */
//        // TODO CHNG & TEST ! ! !
//        val priorityHighAcc = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
//
//        var locationRequest = com.google.android.gms.location.LocationRequest()
//        locationRequest.priority = priorityHighAcc
//        locationRequest.interval = 0
//        locationRequest.fastestInterval = 0
//        locationRequest.numUpdates = 1
//
//        fLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        fLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper())
//    }

    fun createLocationRequest(){
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    val locationCallBack = object: LocationCallback(){
        override fun onLocationResult(locResult: LocationResult) {
            super.onLocationResult(locResult)
            var loc: Location? = locResult.lastLocation
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(){
        // TODO CHNG & TEST ! ! !
        /* Manages the location */
        // Permissions already check in the following checks,
        if (checkGPSPermissions() && checkLocationEnabled()){
            // Get last location
            fLocationProviderClient.lastLocation
                .addOnSuccessListener {
////                requestLocation()
//                    // LocationRequest -> QoS parameters for request, when updates
//                    val priorityHighAcc = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
//                    var locationRequest = com.google.android.gms.location.LocationRequest()
////                    locationRequest.priority = priorityHighAcc
////                    locationRequest.interval = 0
////                    locationRequest.fastestInterval = 0
////                    locationRequest.numUpdates = 1
                    createLocationRequest()

                    // LocationCallback -> Notifications when device location changed or not known
                    // LocationResult -> Where stores the location
                    fLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                    fLocationProviderClient.requestLocationUpdates(
                        locationRequest, locationCallBack, Looper.myLooper()!!)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Localización no encontrada", Toast.LENGTH_SHORT).show()
                }
        }
    }



    /* --- Toolbar and Navigation bars --- */
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

    /* --- NavigationBar --- */
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

    fun goEscapeRoom(){
        var intent = Intent(this, EscapeRoomActivity::class.java)
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