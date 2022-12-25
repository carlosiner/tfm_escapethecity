package uoc.tfm.escapethecity.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import uoc.tfm.escapethecity.R
import uoc.tfm.escapethecity.data.GameUserLogs
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class GLogbookAdapter(private val logList: ArrayList<GameUserLogs>):
    RecyclerView.Adapter<GLogbookAdapter.MyViewHolder>() {

    private lateinit var context: android.content.Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate with the image
        context = parent.context
        val logView  = LayoutInflater.from(parent.context).inflate(
            R.layout.item_log, parent, false)
        return MyViewHolder(logView)
    }

    override fun onBindViewHolder(vHolder: MyViewHolder, position: Int){
        val logs : GameUserLogs = logList[position]
        vHolder.constraintLayout.tag = logs.log_id
        vHolder.lTitle.text = logs.log_title
        val logTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(logs.log_time!!),
            TimeZone.getDefault().toZoneId())
        vHolder.lDate.text = logTime.toString() //TODO TEST
        vHolder.lDescription.text = logs.log_description
        vHolder.lPoints.text = logs.log_points.toString()
    }

    override fun getItemCount(): Int {
        // Get size of list
        return logList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        // Set the item values containers
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.cl_g_log)
        val lTitle: TextView = itemView.findViewById(R.id.tv_game_log_title)
        val lDate: TextView = itemView.findViewById(R.id.tv_game_log_date)
        val lDescription: TextView = itemView.findViewById(R.id.tv_game_log_description)
        val lPoints: TextView = itemView.findViewById(R.id.tv_game_log_points)
    }
}