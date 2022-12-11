package uoc.tfm.escapethecity.data

import com.google.firebase.firestore.GeoPoint

data class GameZones (
    var p_id: String ?= null,
    var p_name: String ?= null,
    var p_coord: GeoPoint ?= null,
    var p_description: String ?= null,
    var p_visited: Boolean = false
)