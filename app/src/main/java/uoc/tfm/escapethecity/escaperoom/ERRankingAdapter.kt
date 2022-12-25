package uoc.tfm.escapethecity.escaperoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uoc.tfm.escapethecity.R
import uoc.tfm.escapethecity.data.UserRankingPosition
import kotlin.collections.ArrayList

class ERRankingAdapter(private val rankList: ArrayList<UserRankingPosition>):
    RecyclerView.Adapter<ERRankingAdapter.MyViewHolder>() {

    private lateinit var context: android.content.Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate with the image
        context = parent.context
        val logView  = LayoutInflater.from(parent.context).inflate(
            R.layout.item_er_ranking, parent, false)
        return MyViewHolder(logView)
    }

    override fun onBindViewHolder(vHolder: MyViewHolder, position: Int){
        val rankItem : UserRankingPosition = rankList[position]
//        vHolder.constraintLayout.tag = logs.log_id
        vHolder.rPosition.text = rankItem.user_position.toString()
        vHolder.rUsername.text = rankItem.user_name
        vHolder.rPoints.text = rankItem.user_points.toString()
    }

    override fun getItemCount(): Int {
        // Get size of list
        return rankList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        // Set the item values containers
//        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.cl_g_log)
        val rPosition: TextView = itemView.findViewById(R.id.tv_er_ranking_position)
        val rUsername: TextView = itemView.findViewById(R.id.tv_er_ranking_username)
        val rPoints: TextView = itemView.findViewById(R.id.tv_er_ranking_points)
    }
}