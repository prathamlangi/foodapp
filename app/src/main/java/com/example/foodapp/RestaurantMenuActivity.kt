package com.example.foodapp

import adapter.RestaurantMenuAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import model.RestaurantMenuData
import org.json.JSONObject
import util.ConnectionManager

class RestaurantMenuActivity : AppCompatActivity() {

    private lateinit var recyclerRestaurantMenu: RecyclerView

    private lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var recyclerAdapter: RestaurantMenuAdapter

    private lateinit var progressLayout: RelativeLayout

    private lateinit var progressBar: ProgressBar

    private var restuarantId: Int? = 0

    val restaurantMenuInfoList = arrayListOf<RestaurantMenuData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)

        recyclerRestaurantMenu =findViewById(R.id.recyclerRestaurantMenu)

        layoutManager = LinearLayoutManager(this)

        progressLayout=findViewById(R.id.progressLayout)

        progressBar=findViewById(R.id.progressbar)
        progressLayout.visibility= View.VISIBLE

        if (intent != null) {
            restuarantId = intent.getIntExtra("id",0)
        } else {
            finish()
            Toast.makeText(
                this@RestaurantMenuActivity,
                "Some Unsuspected Error Occurred",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        if (restuarantId == 0) {
            finish()
            Toast.makeText(
                this@RestaurantMenuActivity,
                "Some Unsuspected Error Occurred",
                Toast.LENGTH_SHORT
            )
                .show()
        }

        val queue = Volley.newRequestQueue(this@RestaurantMenuActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/<restaurant_id>"

        val jsonParams = JSONObject()
        jsonParams.put("id",restuarantId)


        if(ConnectionManager().checkConnectivity(this@RestaurantMenuActivity)){


            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    println("response $it")
                    try {
                        val request=it.getJSONObject("data")
                        val success=request.getBoolean("success")
                        if (success) {
                            val restMenuJsonObject = request.getJSONArray("data")
                            progressLayout.visibility=View.GONE
                            for (i in 0 until restMenuJsonObject.length()){
                                val restJsonObject=restMenuJsonObject.getJSONObject(i)
                                val restObjects= RestaurantMenuData(
                                    restJsonObject.getString("restaurant_id"),
                                    restJsonObject.getString("name"),
                                    restJsonObject.getString("cost_for_one"),
                                )

                                restaurantMenuInfoList.add(restObjects)


                                recyclerAdapter = RestaurantMenuAdapter(this,restaurantMenuInfoList)

                                recyclerRestaurantMenu.layoutManager = layoutManager

                                recyclerRestaurantMenu.adapter = recyclerAdapter

                                recyclerRestaurantMenu.setHasFixedSize(true)

                        }
                        }else {
                            Toast.makeText(
                                this@RestaurantMenuActivity,
                                "Not getting true  Error Occurred",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }catch (e:Exception){
                        Toast.makeText(this@RestaurantMenuActivity,"Some Error Occurred!!!!",Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        this@RestaurantMenuActivity,
                        "Volley Error Occurred",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers=HashMap<String,String>()
                        headers["Content-type"]="application/json"
                        headers["token"]="637e8feaae4684"
                        return headers

                    }
                }
            queue.add(jsonObjectRequest)
        }
        else{
            val dialog= AlertDialog.Builder(this@RestaurantMenuActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Setting"){_,_->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit"){ _, _ ->
                ActivityCompat.finishAffinity(this@RestaurantMenuActivity)
            }
            dialog.create()
            dialog.show()
        }
    }
}