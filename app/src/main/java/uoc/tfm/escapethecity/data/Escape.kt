package uoc.tfm.escapethecity.data

import android.location.Geocoder
import android.sax.Element
import com.google.protobuf.Duration
import com.google.protobuf.Value
import java.net.URL
import java.security.Key
import java.security.Timestamp
import java.util.*
import kotlin.collections.HashMap

data class Escape(
    var name: String ?= null,
    var info: String ?= null,
    var image: String ?= null,
    var enabled: Boolean ?= null,
    var city: String ?= null,
    var location: com.google.firebase.firestore.GeoPoint ?= null,
    var escape_duration: Long ?= null,
    var difficulty: String ?= null,
    var initial_item: String ?= null,

    // Copied items for user per escape db
    var id: String ?= null,
    var achievements: HashMap<String, ERAchievements> = hashMapOf(),
    var items: HashMap<String, GameItems> = hashMapOf(),
    var zones: HashMap<String, GameZones> = hashMapOf(),
    var trials: HashMap<String, GameTrials> = hashMapOf()

)