package com.example.foodapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import util.ConnectionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var enterPhone: EditText
    private lateinit var enterPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtForgetPass: TextView
    private lateinit var txtRegister: TextView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        creating shared preference object
        sharedPreferences =getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        //checking by SharedPreferences is loggedIn or not
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        enterPhone = findViewById(R.id.et_phone)
        enterPass = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        txtForgetPass = findViewById(R.id.txt_forget_pass)
        txtRegister = findViewById(R.id.txt_register)


        btnLogin.setOnClickListener {
//            val intent = Intent(this@LoginActivity, MainActivity::class.java)
//            startActivity(intent)
            val menterPhone = enterPhone.text.toString()
            val menterPass = enterPass.text.toString()
            if (validateInput(menterPhone, menterPass)) {
                authenticateWithAPI(menterPhone, menterPass)
            }
        }

//        Go to Forget Activity
        txtForgetPass.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgetPassActivity::class.java)
            startActivity(intent)
        }

//        Go to Register Activity
        txtRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

//    Check length of input mobile number and password
    private fun validateInput(menterPhone: String, menterPass: String): Boolean {
        if (menterPhone.length != 10) {
            Toast.makeText(this@LoginActivity, "Enter Valid Number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (menterPass.length <= 4) {
            Toast.makeText(this@LoginActivity, "Enter Valid Password", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    //authenticateWithAPI
    private fun authenticateWithAPI(menterPhone: String, menterPass: String) {
        val queue = Volley.newRequestQueue(this@LoginActivity)
        val url = "http://13.235.250.119/v2/login/fetch_result/"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number", menterPhone)
        jsonParams.put("password", menterPass)

        if (ConnectionManager().checkConnectivity(this@LoginActivity)) {

            val request =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    println("response $it")
                    try {
                        val dataRequest = it.getJSONObject("data")
                        val success = dataRequest.getBoolean("success")
                        if (success) {
                            val dataResponse = dataRequest.getJSONObject("data")
                            savePreference(dataResponse)
                            val i = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(i)
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Not getting true  Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@LoginActivity, "Some Error Occurred!!!!", Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {
                    Toast.makeText(
                        this@LoginActivity, "Getting Volley Error ", Toast.LENGTH_SHORT
                    ).show()

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "637e8feaae4684"
                        return headers
                    }
                }
            queue.add(request)
        } else {
            val dialog = AlertDialog.Builder(this@LoginActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Setting") { _, _ ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(this@LoginActivity)
            }
            dialog.create()
            dialog.show()

        }
    }

    fun savePreference(userData: JSONObject) {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        sharedPreferences.edit().putString("user_id", userData.getString("user_id")).apply()
        sharedPreferences.edit().putString("name", userData.getString("name")).apply()
        sharedPreferences.edit().putString("email", userData.getString("email")).apply()
        sharedPreferences.edit().putString("mobile_number", userData.getString("mobile_number"))
            .apply()
        sharedPreferences.edit().putString("address", userData.getString("address")).apply()
    }

}