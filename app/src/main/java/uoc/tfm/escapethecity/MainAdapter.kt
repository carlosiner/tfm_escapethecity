package uoc.tfm.escapethecity

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.grpc.Context
import uoc.tfm.escapethecity.data.Escape
import uoc.tfm.escapethecity.data.ItemsViewModel

class MainAdapter(private val erList: ArrayList<Escape>) : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    private lateinit var context: android.content.Context // TODO necesario ?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        context = parent.context
        val itemView  = LayoutInflater.from(parent.context).inflate(
            R.layout.item_escape_room, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(vHolder: MyViewHolder, position: Int){
        val items : Escape = erList[position]
        vHolder.constraintLayout.tag = items.id

        // Image from URL (string)
//        var image: Bitmap? = null
//        var drawable : Drawable? = null
//        val `in` = java.net.URL(items.image).openStream()
//        image = BitmapFactory.decodeStream(`in`)
//        drawable = (Resources.getSystem(), items.image)
        Glide.with(context).load(items.image).fitCenter().into(vHolder.imageView)

//        vHolder.imageView.setImageResource(R.drawable.game_el_secreto)
//        vHolder.imageView.setImageResource(R.drawable.game_el_secreto)
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