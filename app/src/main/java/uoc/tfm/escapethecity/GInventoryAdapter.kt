package uoc.tfm.escapethecity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uoc.tfm.escapethecity.data.GameItems

class GInventoryAdapter(private val itemList: ArrayList<GameItems>) :
    RecyclerView.Adapter<GInventoryAdapter.MyViewHolder>() {

    private lateinit var context: android.content.Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       // Inflate with the image
        context = parent.context
        val itemView  = LayoutInflater.from(parent.context).inflate(
            R.layout.item_inventory, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(vHolder: MyViewHolder, position: Int){
        val items : GameItems = itemList[position]
        vHolder.constraintLayout.tag = items.i_id
        Glide.with(context).load(items.i_img).fitCenter().into(vHolder.imageView)
        vHolder.textView.text = items.i_name
    }

    override fun getItemCount(): Int {
        // Get size of list
        return itemList.size
    }

    class MyViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
        // Set the item values containers
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.ll_g_item)
        val imageView: ImageView = itemView.findViewById(R.id.iv_game_item_image)
        val textView: TextView = itemView.findViewById(R.id.tv_game_item_name)
    }
}