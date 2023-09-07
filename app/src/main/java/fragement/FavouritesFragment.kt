@file:Suppress("DEPRECATION")

package fragement

import adapter.FavouriteAdapter
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodapp.R
import database.ResDatabase
import database.RestaurantEntity


class FavouritesFragment : Fragment() {

    private lateinit var recyclerfavourite: RecyclerView

    private lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var recyclerAdapter: FavouriteAdapter

    private lateinit var progressLayout: RelativeLayout

    private lateinit var progressBar: ProgressBar

    private var dbRestaurantList=listOf<RestaurantEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        recyclerfavourite = view.findViewById(R.id.recycler_favourite)

        layoutManager = LinearLayoutManager(activity)

        progressLayout = view.findViewById(R.id.progressLayout)

        progressBar = view.findViewById(R.id.progressbar)

        progressLayout.visibility = View.VISIBLE

        dbRestaurantList=RetriveFavourites(activity as Context).execute().get()

        if(activity !=null){
            progressLayout.visibility=View.GONE
            recyclerAdapter= FavouriteAdapter(activity as Context,dbRestaurantList)
            recyclerfavourite.adapter=recyclerAdapter
            recyclerfavourite.layoutManager=layoutManager
        }



        return view
    }

    class RetriveFavourites(val context:Context):AsyncTask<Void,Void,List<RestaurantEntity>>() {
        override fun doInBackground(vararg p0: Void?): List<RestaurantEntity> {
            val db= Room.databaseBuilder(context,ResDatabase::class.java,"res-db").build()
            return db.restaurantDao().getAllRes()
        }
    }
}