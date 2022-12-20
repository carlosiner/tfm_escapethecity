package uoc.tfm.escapethecity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import uoc.tfm.escapethecity.data.GameZones

class GMapActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback {

    // Common Drawer definition
    private lateinit var drawerL: DrawerLayout
    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: GameZones

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmap)
        loadTopBar()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        // From first non-finish trial, get location
        var trials = currentERUser.trials
        var flagFinish = false
        for (i in trials){
            if(!i.value.t_finished){
                currentLocation = currentERUser.zones[i.value.t_id_zone]!!
                flagFinish = false
                // Get position
                val position = LatLng(
                    currentLocation.p_coord!!.latitude, currentLocation.p_coord!!.longitude)

                // Add a marker in the map to select the location
                mMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(currentLocation.p_name))

                // Add the zone to search
                mMap.addCircle(CircleOptions()
                    .center(position)
                    .radius(50.0)
                    .fillColor(0x22ffff00) // Transparent yellow
                    .strokeColor(Color.YELLOW))

                // Center the camera in that position and zoom it
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 17f))
                break
            }
            else{
                flagFinish = true
            }
        }

        if (flagFinish){
            Toast.makeText(this,getString(R.string.b_game_map_finish), Toast.LENGTH_SHORT).show()
            goBack()
        }


    }


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