package uoc.tfm.escapethecity
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.ZoneId
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Looper
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import uoc.tfm.escapethecity.access.RegistrationActivity
import uoc.tfm.escapethecity.data.*
import uoc.tfm.escapethecity.escaperoom.EscapeRoomActivity
import uoc.tfm.escapethecity.game.GameActivity
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
        var currentGameTrialValue: GameTrials = GameTrials()

        /* Game - Timer */
        var endTime: Long = 0

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
    fun getImageFromURL(context: Context, imageULR: String, imageView: ImageView, isGif: Boolean=false){
        /* Get image from URL (String) and insert into a view*/
        if (!isGif){
            Glide.with(context).load(imageULR).fitCenter().into(imageView)
        }
        else{
            Glide.with(context).asGif().load(imageULR).fitCenter().into(imageView)
        }

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

    fun createDialogAchievement(title: String, message: String){
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .show()
    }

    fun surlineText(tView: TextView, text: String){
        /* To surline the text */
        val sString = SpannableString(text)
        sString.setSpan(UnderlineSpan(), 0, sString.length, 0)
        tView.text = sString
    }

    /* --- Time & Date --- */
    fun checkERStart(): Boolean {
        /* Checks if the Game can start, the user is in the right time */
        val currentTime = getCurrentTimeInSeconds()
        val startTime = currentERUser.user_date_selected
        val minAdd = 60 * 30 // 30m difference
        return (currentTime + minAdd) >= startTime
    }

    fun getCurrentTimeInSeconds(): Long {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
    }

    fun timerSetEndDate(){
        /* Simple calcultation of the end of the Game */
        val selectedStartTime = currentERUser.user_date_selected
        val escapeTime = currentERContent.escape_duration
        endTime = selectedStartTime + escapeTime!!
    }


    /* --- User data --- */
    fun clearGameUserData(){
        /* To remove any content from the game,
        * with the exception of Achievements
        * */
        currentERUser.user_status = 0
        currentERUser.user_points = 0
        currentERUser.trials = currentERContent.trials
        currentERUser.items = currentERContent.items
        currentERUser.zones = currentERContent.zones
        currentERUser.user_logs = hashMapOf()

    }

    fun setUserLog(title: String, description: String, points: Int=0){
        /* Generates a new user log with the defined information */
        val mapLogs = currentERUser.user_logs
        var newLog = GameUserLogs()
        var newId = "ul"
        var lastId = 0
        newLog.log_title = title
        newLog.log_time = getCurrentTimeInSeconds()
        newLog.log_description = description
        newLog.log_points = points

        if (mapLogs.isNotEmpty()){
            for (i in mapLogs){
                var thisKey =  (i.key.split("ul")[1]).toInt()
                if (lastId < thisKey){
                    lastId = thisKey
                }
            }
            newId += lastId+1
        }
        else{
            newId += "0"
        }
        newLog.log_id = newId
        currentERUser.user_logs[newId] = newLog

        // Add points
        if (points!=0){
            setTotalPoints(points)
        }
    }

    private fun setTotalPoints(points: Int){
        /* Count the total user points */
        currentERUser.user_points += points
        if (currentERUser.user_points <= 0){
            // User cannot have negative points
            currentERUser.user_points = 0
        }
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

    /* --- Triañs --- */
    fun getSoleItem(itemId: String){
        if (!currentERUser.items[itemId]!!.i_used){
            // Item not used
            currentERUser.items[itemId]!!.i_found = true
            // Send notification (Toast)
            Toast.makeText(this,
                getString(R.string.b_game_investigation_get_item)
                        + " "
                        + currentERUser.items[itemId]!!.i_name,
                Toast.LENGTH_SHORT).show()
            // Log event
            var itemName = currentERUser.items[itemId]!!.i_name
            setUserLog(
                getString(R.string.tv_game_userlog_title_GI) + itemName,
                getString(R.string.tv_game_userlog_desc_GI)
            )
        }
    }


    fun getItems(currentGameTrialValue: GameTrials){
        /* At the end of the trial get the item */
        if(currentGameTrialValue.t_id_item_found != ""){
            // Change objet status
            currentERUser.items[currentGameTrialValue.t_id_item_found]!!
                .i_found=true

            // Send notification (Toast)
            Toast.makeText(this,
                getString(R.string.b_game_investigation_get_item)
                        + " "
                        + currentERUser.items[currentGameTrialValue.t_id_item_found]!!.i_name,
                Toast.LENGTH_SHORT).show()

            // Log event
            var itemName = currentERUser.items[currentGameTrialValue.t_id_item_found]!!.i_name
            setUserLog(
                getString(R.string.tv_game_userlog_title_GI) + itemName,
                getString(R.string.tv_game_userlog_desc_GI),
                20
            )
        }
    }

    fun getAchievements(currentGameTrialValue: GameTrials){
        /* At the end of the trial get the achievement */
        if(currentGameTrialValue.t_id_achievement != ""){
            currentERUser.achievements[currentGameTrialValue.t_id_achievement]!!
                .ac_active=true
            Toast.makeText(this,
                getString(R.string.b_game_investigation_get_achievement)
                        + " "
                        + currentERUser.achievements[currentGameTrialValue.t_id_achievement]!!.ac_name,
                Toast.LENGTH_SHORT).show()

            // Log event
            var achiName = currentERUser.achievements[currentGameTrialValue.t_id_achievement]!!.ac_name
            setUserLog(
                getString(R.string.tv_game_userlog_title_GA) + achiName,
                getString(R.string.tv_game_userlog_desc_GA),
                20
            )
        }
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
                DialogInterface.OnClickListener{ _, _ ->
                    activateGPS()
                })
            .setNegativeButton(getString(R.string.tv_game_gps_dialog_cancel),
                DialogInterface.OnClickListener{ _, _ ->
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
        /* Manages the location */
        // Permissions already check in the following checks,
        if (checkGPSPermissions() && checkLocationEnabled()){
            // Get last location
            fLocationProviderClient.lastLocation
                .addOnSuccessListener {
                    createLocationRequest()
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

        if (userInfo.username == null || userInfo.email == null){
            loadUser()
        }

        var tvUser: TextView = lateralView.findViewById(R.id.menu_profile_email)
        tvUser.text = userInfo.email
        tvUser = lateralView.findViewById(R.id.menu_profile_name)
        tvUser.text = userInfo.username
        val tvUserImage: ImageView = lateralView.findViewById(R.id.menu_profile_image)
        getImageFromURL(lateralView.context, userInfo.image!!, tvUserImage)
    }

    /* Functions from NavigationMenu (lateral menu) */
    fun goMain(){
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    fun goMyGames(){
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun goEscapeRoom(){
        var intent = Intent(this, EscapeRoomActivity::class.java)
        startActivity(intent)
    }

    fun goGame() {
        /* After finishing a trial or finishing the game (in Map)
        * go back to the Game menu
        * */
        var intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
        var intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    /* Other functions */
    fun goBack(){
        finish()
    }

}