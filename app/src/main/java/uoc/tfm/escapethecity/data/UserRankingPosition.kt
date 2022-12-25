package uoc.tfm.escapethecity.data

data class UserRankingPosition(
    var user_name: String ?= null,
    var user_points: Int = 0,
    var user_position: Int = 0
)