package fragement

import adapter.HomeAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodapp.R
import model.RestaurantData
import org.json.JSONException
import util.ConnectionManager


class HomeFragment : Fragment() {

    private lateinit var recyclerhome: RecyclerView

    private lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var recyclerAdapter: HomeAdapter

    private lateinit var progressLayout: RelativeLayout

    private lateinit var progressBar: ProgressBar

    val restaurantInfoList = arrayListOf<RestaurantData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerhome = view.findViewById(R.id.recycler_home)

        layoutManager = LinearLayoutManager(activity)

        progressLayout=view.findViewById(R.id.progressLayout)

        progressBar=view.findViewById(R.id.progressbar)
        progressLayout.visibility=View.VISIBLE


        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)){
            val jasonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    //here we will handle the response

                    try{
                        Log.e("Response","Pass")
                        progressLayout.visibility=View.GONE
                        val request=it.getJSONObject("data")
                        val success=request.getBoolean("success")
                        if(success){
                            val resArray=request.getJSONArray("data")
                            for (i in 0 until resArray.length()){
                                val restJsonObject=resArray.getJSONObject(i)
                                val restObjects=RestaurantData(
                                    restJsonObject.getString("name"),
                                    restJsonObject.getString("rating"),
                                    restJsonObject.getString("cost_for_one"),
                                    restJsonObject.getString("image_url"),
                                )
                                restaurantInfoList.add(restObjects)

                                recyclerAdapter =HomeAdapter(activity as Context,restaurantInfoList)

                                recyclerhome.layoutManager = layoutManager

                                recyclerhome.adapter = recyclerAdapter

                                recyclerhome.setHasFixedSize(true)


                            }
                        }else {
                            (
                                    Toast.makeText(activity as Context,"Some Error Occurred success false!!!!", Toast.LENGTH_SHORT).show()
                                    )
                        }
                    }catch (e: JSONException){
                        Toast.makeText(activity as Context,"Some Error Occurred!!!!", Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(activity as Context,"Volley Occurred!!!", Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "637e8feaae4684"
                        return headers
                    }
                }
            queue.add(jasonObjectRequest)
        }
        else{
            val dialog= AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Setting"){_,_->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){ _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }




}