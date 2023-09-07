@file:Suppress("DEPRECATION")

package adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodapp.R
import com.example.foodapp.RestaurantMenuActivity
import com.squareup.picasso.Picasso
import database.ResDatabase
import database.RestaurantEntity
import model.RestaurantData

class HomeAdapter(private val context: Context, private val itemList: ArrayList<RestaurantData>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtResName: TextView = view.findViewById(R.id.txtResName)
        val txtResPrice: TextView = view.findViewById(R.id.txtResPrice)
        val txtResRating: TextView = view.findViewById(R.id.txtResRating)
        val imgResImage: ImageView = view.findViewById(R.id.imgResImage)
        val btnAddFav: ImageView = view.findViewById(R.id.imgResAddFavourite)
        val rescontent: LinearLayout = view.findViewById(R.id.res_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_single_row_home, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val res = itemList[position]
        holder.txtResName.text = res.restaurantName
        holder.txtResPrice.text = res.restaurantPrice.toString()
        holder.txtResRating.text = res.restaurantRating
//        holder.imgResImage.setImageResource(res.RestaurantImage)
        Picasso.get().load(res.restaurantImage).error(R.drawable.app_logo).into(holder.imgResImage)

        holder.rescontent.setOnClickListener {
            val intent = Intent(context, RestaurantMenuActivity::class.java)
            intent.putExtra("id", res.restaurantId)
            context.startActivity(intent)
        }
        val restaurantEntity = RestaurantEntity(
            res.restaurantId,
            res.restaurantName,
            res.restaurantRating,
            res.restaurantPrice.toString(),
            res.restaurantImage
        )
        val checkFav = DBAsyncTask(context, restaurantEntity, 1).execute()
        val isFav = checkFav.get()
        if (isFav) {
            holder.btnAddFav.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            holder.btnAddFav.setImageResource(R.drawable.baseline_favorite_border_24)
        }
        holder.btnAddFav.setOnClickListener {

            if (!DBAsyncTask(context, restaurantEntity, 1).execute()
                    .get()
            ) {
                val async =
                    DBAsyncTask(context, restaurantEntity, 2).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "Restaurant added to favourites!",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.btnAddFav.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    Toast.makeText(
                        context,
                        "Some error occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val async =
                    DBAsyncTask(context, restaurantEntity, 3).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "Restaurant removed from favourites!",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.btnAddFav.setImageResource(R.drawable.baseline_favorite_border_24)
                } else {
                    Toast.makeText(
                        context,
                        "Some error occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    class DBAsyncTask(
        private val Context: Context,
        private val restaurantEntity: RestaurantEntity,
        private val mode: Int
    ) :
        AsyncTask<Void, Void, Boolean>() {
        private val db = Room.databaseBuilder(Context, ResDatabase::class.java, "res-db").build()

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Boolean {
            when (mode) {
                1 -> {
                    val res: RestaurantEntity? = db.restaurantDao()
                        .getRestaurantId(restaurantEntity.restaurant_id.toString())
                    db.close()
                    return res != null
                }

                2 -> {
                    db.restaurantDao().insertRes(restaurantEntity)
                    db.close()
                    return true
                }

                3 -> {
                    db.restaurantDao().deleteRes(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }
}