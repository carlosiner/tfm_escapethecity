package uoc.tfm.escapethecity.data

import com.google.firebase.Timestamp

data class GameUserLogs (
    var log_title: String ?= null,
    var log_description: String ?= null,
    var log_time: Timestamp ?=null
)