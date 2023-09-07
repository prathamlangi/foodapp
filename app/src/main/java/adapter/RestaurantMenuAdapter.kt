package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.RestaurantMenuActivity
import model.RestaurantMenuData

class RestaurantMenuAdapter(
    private val context: RestaurantMenuActivity,
    private val itemList: ArrayList<RestaurantMenuData>
) :
    RecyclerView.Adapter<RestaurantMenuAdapter.RestaurantMenuViewHolder>() {

    class RestaurantMenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtResMenuNo: TextView = view.findViewById(R.id.txtRestaurantMenuNo)
        val txtResMenuName: TextView = view.findViewById(R.id.txtRestaurantMenuName)
        val txtResMenuPrice: TextView = view.findViewById(R.id.txtRestaurantMenuPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_single_row_restaurant_menu, parent, false)
        return RestaurantMenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RestaurantMenuViewHolder, position: Int) {
        val resMenu = itemList[position]
        val displayText = "${position + 1}.$resMenu"
        holder.txtResMenuNo.text = displayText
        holder.txtResMenuName.text = resMenu.restaurantMenuName
        holder.txtResMenuPrice.text = resMenu.restaurantMenuPrice

    }
}