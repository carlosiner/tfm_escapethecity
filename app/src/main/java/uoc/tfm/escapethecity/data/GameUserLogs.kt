package uoc.tfm.escapethecity.data

import com.google.firebase.Timestamp

data class GameUserLogs (
    var log_id: String ?= null,
    var log_title: String ?= null,
    var log_description: String ?= null,
    var log_points: Int = 0,
    var log_time: Long ?= null
)