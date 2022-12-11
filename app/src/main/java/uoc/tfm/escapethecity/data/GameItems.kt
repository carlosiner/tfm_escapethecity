package uoc.tfm.escapethecity.data

data class GameItems (
    var i_id: String ?= null,
    var i_name: String ?= null,
    var i_img: String ?= null,
    var i_found: Boolean = false,
    var i_used: Boolean = false,
    var i_description: String ?= null
)