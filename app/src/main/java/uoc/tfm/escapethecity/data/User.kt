package uoc.tfm.escapethecity.data

data class User(
    var username: String,
    var registrationDate: String,
    var email: String,
    val role: String
) {

}