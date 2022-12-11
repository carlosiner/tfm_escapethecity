package uoc.tfm.escapethecity.data

import android.location.Geocoder
import android.sax.Element
import com.google.protobuf.Duration
import com.google.protobuf.Value
import java.net.URL
import java.security.Key
import java.security.Timestamp
import java.util.*

data class Escape(
    var name: String ?= null,
    var info: String ?= null,
    var image: String ?= null,
    var enabled: Boolean ?= null,
    var city: String ?= null,
    var location: com.google.firebase.firestore.GeoPoint ?= null,
    var escape_duration: Long ?= null,

    // Copied items for user per escape db
    var id: String ?= null,
    var achievements: ArrayList<ERAchievements> = arrayListOf(),
    var items: ArrayList<GameItems> = arrayListOf(),
    var zones: ArrayList<GameZones> = arrayListOf(),
    var trials: ArrayList<GameTrials> = arrayListOf()

)