package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.squareup.picasso.Picasso
import database.RestaurantEntity
import model.RestaurantData

class FavouriteAdapter (private val context: Context, private val itemList:List<RestaurantEntity>) :
    RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtResName: TextView = view.findViewById(R.id.txtResName)
        val txtResPrice: TextView = view.findViewById(R.id.txtResPrice)
        val txtResRating: TextView = view.findViewById(R.id.txtResRating)
        val imgResImage: ImageView = view.findViewById(R.id.imgResImage)

        //        val btnAddFav: ImageView = view.findViewById(R.id.imgResAddFavourite)
        val favcontent: LinearLayout = view.findViewById(R.id.fav_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_single_row_favourite, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val fav = itemList[position]
        holder.txtResName.text = fav.restaurantName
        holder.txtResPrice.text = fav.restaurantPrice.toString()
        holder.txtResRating.text = fav.restaurantRating
        Picasso.get().load(fav.restaurantImage).error(R.drawable.app_logo).into(holder.imgResImage)
    }
}
