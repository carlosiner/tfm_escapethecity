package uoc.tfm.escapethecity.data

import java.util.SimpleTimeZone

data class GameTrials (
    var t_id: String ?= null,
    var t_name: String ?= null,
    var t_description: String ?= null,
    var t_finished: Boolean = false,
    var t_solution_tip: String ?= null,
    var t_resource: String ?= null,
    var t_solution: String ?= null,
    var t_points: Int = 0,
    var t_totalPoints: Int = 0,
    // Other related ids
    var t_id_zone: String ?= null,
    var t_id_item_found: String ?= null,
    var t_id_item_used: String ?= null,
    var t_id_achievement: String ?= null,
    // Clues
    var t_clue1: String ?= null,
    var t_clue1_activated: Boolean = false,
    var t_clue2: String ?= null,
    var t_clue2_activated: Boolean = false,
    var t_clue3: String ?= null,
    var t_clue3_activated: Boolean = false,
)