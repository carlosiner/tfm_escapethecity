package uoc.tfm.escapethecity.data

import com.google.firebase.Timestamp
import java.util.ArrayList
import java.util.HashMap

data class UserEscape(
    var user_status: Int = 0, // Multiple status 0,1,2,3,4
    var user_date_selected: Long = 0,
    var user_points: Int = 0,
    var timer_activated: Boolean = false,
    var timer_last_time: Int = 0,
    var user_logs:  ArrayList<GameUserLogs> = arrayListOf(),

    // Escape information copied & modified by each user
    var id: String ?= null,
    var achievements: ArrayList<ERAchievements> = arrayListOf(),
    var items: ArrayList<GameItems> = arrayListOf(),
    var zones: ArrayList<GameZones> = arrayListOf(),
    var trials: ArrayList<GameTrials> = arrayListOf()
)