package uoc.tfm.escapethecity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uoc.tfm.escapethecity.data.Escape

class MainAdapter(private val erList: ArrayList<Escape>) :
    RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    private lateinit var context: android.content.Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        context = parent.context
        val itemView  = LayoutInflater.from(parent.context).inflate(
            R.layout.item_escape_room, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(vHolder: MyViewHolder, position: Int){
        val items : Escape = erList[position]
        vHolder.constraintLayout.tag = items.id
        Glide.with(context).load(items.image).fitCenter().into(vHolder.imageView)
        vHolder.textView.text = items.name
    }

    override fun getItemCount(): Int {
        return erList.size
    }

    class MyViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.rlDummyEscapeRoom)
        val imageView: ImageView = itemView.findViewById(R.id.imDummy)
        val textView: TextView = itemView.findViewById(R.id.tvDummy)
    }

}