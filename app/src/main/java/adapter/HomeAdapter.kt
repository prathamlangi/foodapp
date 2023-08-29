package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.squareup.picasso.Picasso
import model.RestaurantData

class HomeAdapter(private val context: Context, private val itemList:ArrayList<RestaurantData>):RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtResName:TextView=view.findViewById(R.id.txtResName)
        val txtResPrice:TextView=view.findViewById(R.id.txtResPrice)
        val txtResRating:TextView=view.findViewById(R.id.txtResRating)
        val imgResImage:ImageView=view.findViewById(R.id.imgResImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_single_row_home,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val res =itemList[position]
        holder.txtResName.text= res.RestaurantName
        holder.txtResPrice.text=res.RestaurantPrice
        holder.txtResRating.text=res.RestaurantRating
//        holder.imgResImage.setImageResource(res.RestaurantImage)
        Picasso.get().load(res.RestaurantImage).error(R.drawable.app_logo).into(holder.imgResImage)
    }
}