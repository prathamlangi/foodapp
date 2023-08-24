package com.example.foodapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import util.ConnectionManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var enterRegisterName: EditText
    private lateinit var enterRegisterEmail: EditText
    private lateinit var enterRegisterPhone: EditText
    private lateinit var enterRegisterAddress: EditText
    private lateinit var enterRegisterPassword: EditText
    private lateinit var enterRegisterConPassword: EditText
    private lateinit var btnRegister: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title = "Register Yourself"

        enterRegisterName = findViewById(R.id.et_register_name)
        enterRegisterEmail = findViewById(R.id.et_register_email)
        enterRegisterPhone = findViewById(R.id.et_register_phone)
        enterRegisterAddress = findViewById(R.id.et_register_delivery)
        enterRegisterPassword = findViewById(R.id.et_register_password)
        enterRegisterConPassword = findViewById(R.id.et_register_con_password)
        btnRegister= findViewById(R.id.btnRegister)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        btnRegister.setOnClickListener{
            val menterRegisterName = enterRegisterName.text.toString()
            val menterRegisterEmail = enterRegisterEmail.text.toString()
            val menterRegisterPhone = enterRegisterPhone.text.toString()
            val menterRegisterAddress = enterRegisterAddress.text.toString()
            val menterRegisterPassword = enterRegisterPassword.text.toString()
            val menterRegisterConPassword = enterRegisterConPassword.text.toString()
            if (validateInput(menterRegisterPhone,menterRegisterPassword,menterRegisterConPassword,menterRegisterEmail)){
                authenticateWithAPI(menterRegisterName, menterRegisterEmail, menterRegisterPhone,menterRegisterAddress, menterRegisterPassword)
            }
        }


    }

    //Check length of input mobile number and password and confirm password
    private fun validateInput(menterRegisterPhone: String,menterRegisterPassword:String,menterRegisterConPassword: String,menterRegisterEmail: String): Boolean {

        if (menterRegisterPhone.length != 10) {
            Toast.makeText(this@RegisterActivity, "Enter Valid Number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (menterRegisterPassword.length<= 4){
            Toast.makeText(this@RegisterActivity, "Enter Valid Password", Toast.LENGTH_SHORT).show()
            return false
        }
        if (menterRegisterPassword==menterRegisterConPassword){
            Toast.makeText(this@RegisterActivity, "Enter Valid Password and Confirm Password", Toast.LENGTH_SHORT).show()
            return false
        }
        if(menterRegisterEmail.isEmpty()){
            val emailPattern = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
            emailPattern.matches(menterRegisterEmail)
            Toast.makeText(this@RegisterActivity, "Enter Valid Email", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun authenticateWithAPI(menterRegisterName:String, menterRegisterEmail:String, menterRegisterPhone: String, menterRegisterAddress:String, menterRegisterPassword: String) {
        val queue = Volley.newRequestQueue(this@RegisterActivity)
        val url = "http://13.235.250.119/v2/register/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put("name",menterRegisterName)
        jsonParams.put("email",menterRegisterEmail)
        jsonParams.put("mobile_number", menterRegisterPhone)
        jsonParams.put("address", menterRegisterAddress)
        jsonParams.put("password", menterRegisterPassword)

        if (ConnectionManager().checkConnectivity(this@RegisterActivity)) {

            val request =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    println("response $it")
                    try {
                        val dataRequest = it.getJSONObject("data")
                        val success = it.getBoolean("success")
                        if (success) {
                            val dataResponse = dataRequest.getJSONObject("data")
                            savePreference(dataResponse)
                            val i = Intent(this@RegisterActivity, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Not getting true  Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@RegisterActivity, "Some Error Occurred!!!!", Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {
                    Toast.makeText(
                        this@RegisterActivity, "Getting Volley Error ", Toast.LENGTH_SHORT
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
            val dialog = AlertDialog.Builder(this@RegisterActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Setting") { _, _ ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(this@RegisterActivity)
            }
            dialog.create()
            dialog.show()

        }
    }
    fun savePreference(userData: JSONObject) {
        sharedPreferences.edit().putString("user_id", userData.getString("user_id")).apply()
        sharedPreferences.edit().putString("name", userData.getString("name")).apply()
        sharedPreferences.edit().putString("email", userData.getString("email")).apply()
        sharedPreferences.edit().putString("mobile_number", userData.getString("mobile_number"))
            .apply()
        sharedPreferences.edit().putString("address", userData.getString("address")).apply()
    }
}