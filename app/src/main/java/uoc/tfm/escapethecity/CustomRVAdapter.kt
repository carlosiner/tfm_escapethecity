package uoc.tfm.escapethecity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uoc.tfm.escapethecity.data.ItemsViewModel

class CustomRVAdapter (private val itemList: List<ItemsViewModel>) : RecyclerView.Adapter<CustomRVAdapter.ViewHolder>() {

    // Override to generate a new vview holder to add the items from the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_achievement,
            parent,
            false)
        return ViewHolder(view)
    }

    // Override Binding to attach the parameters from the items
    override fun onBindViewHolder(vHolder: ViewHolder, pos: Int) {
        val items = itemList[pos]
        vHolder.linearLayout.tag = items.tag
        vHolder.imageView.setImageResource(items.image)
        vHolder.textView.text = items.text

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
