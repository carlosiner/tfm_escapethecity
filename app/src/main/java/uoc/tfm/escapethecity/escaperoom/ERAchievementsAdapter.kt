package uoc.tfm.escapethecity.escaperoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uoc.tfm.escapethecity.R
import uoc.tfm.escapethecity.data.ItemsViewModel

class ERAchievementsAdapter (private val itemList: List<ItemsViewModel>) : RecyclerView.Adapter<ERAchievementsAdapter.ViewHolder>() {

    private lateinit var context: android.content.Context

    // Override to generate a new vview holder to add the items from the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_achievement, parent, false)
        return ViewHolder(view)
    }

    // Override Binding to attach the parameters from the items
    override fun onBindViewHolder(vHolder: ViewHolder, pos: Int) {
        val items = itemList[pos]
        vHolder.linearLayout.tag = items.tag
        vHolder.textView.text = items.text
        if (items.image == ""){
            vHolder.imageView.setImageResource(R.drawable.trophy)
        }
        else{
            Glide.with(context).load(items.image).fitCenter().into(vHolder.imageView)
        }


    }

    // Implemented method added
    override fun getItemCount(): Int {
        return itemList.size
    }

    // ViewHolder to add the parameters
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val linearLayout: LinearLayout = itemView.findViewById(R.id.ll_a_container)
        val imageView: ImageView = itemView.findViewById(R.id.iv_a_imageX)
        val textView: TextView = itemView.findViewById(R.id.tv_a_textX)
    }


}
