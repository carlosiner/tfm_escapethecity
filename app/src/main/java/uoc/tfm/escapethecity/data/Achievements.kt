package uoc.tfm.escapethecity.data

data class Achievements(
    var ac_id: String ?= null
    ,var ac_name: String ?= null
    ,var ac_active: Boolean = false
    ,var ac_image: String ?= null
)