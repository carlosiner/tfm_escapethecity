package uoc.tfm.escapethecity.data

import android.net.Uri

data class User(
    var username: String ?= null,
    var registrationDate: String ?= null,
    var email: String ?= null,
    var role: String ?= null,
    var image: String ?= null
)