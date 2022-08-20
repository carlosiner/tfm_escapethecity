package uoc.tfm.escapethecity.data

import android.sax.Element
import com.google.protobuf.Value
import java.security.Key
import java.util.*

data class Escape(
    var name: String ?= null
    ,var info: String ?= null
    ,var image: String ?= null
    ,var enabled: Boolean ?= null
    //,var achievements: List<String> // Fallo !
    //,var achievements: ArrayList<String> = arrayListOf() // Works!
    ,var achievements: ArrayList<HashMap<String,Any>> = arrayListOf() // Works!
)