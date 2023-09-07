package com.example.foodapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
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

class OTPActivity : AppCompatActivity() {

    private lateinit var enterOtp:EditText
    private lateinit var enterPassword:EditText
    private lateinit var enterConPassword:EditText
    private lateinit var btnSubmit:EditText

    private var userNumber: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)

        enterOtp=findViewById(R.id.et_otp)
        enterPassword=findViewById(R.id.et_reset_password)
        enterConPassword=findViewById(R.id.et_reset_con_password)
        btnSubmit=findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener{
            val menterOtp=enterOtp.text.toString()
            val menterPassword=enterPassword.text.toString()
            val menterConPassword=enterConPassword.text.toString()
            if (validateInput(menterOtp, menterPassword, menterConPassword)){
                resetPass(menterOtp, menterPassword)
            }
        }
    }
    private fun validateInput(menterOtp:String, menterPassword:String, menterConPassword:String ):Boolean{
        if (menterOtp.length!=4){
            Toast.makeText(this@OTPActivity, "Enter Valid OTP", Toast.LENGTH_SHORT).show()
            return false
        }
        if (menterPassword.length<=4){
            Toast.makeText(this@OTPActivity, "Enter Valid Password", Toast.LENGTH_SHORT).show()
            return false
        }
        if (menterPassword !=menterConPassword){
            Toast.makeText(this@OTPActivity, "Enter Valid Confirm Passwrod", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun resetPass(menterOtp:String, menterPassword:String){

        if (intent != null) {
            userNumber = intent.getStringExtra("mobile_number")
        } else {
            finish()
            Toast.makeText(
                this@OTPActivity,
                "Some Unsuspected Error Occurred",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        if (userNumber == null) {
            finish()
            Toast.makeText(
                this@OTPActivity,
                "Some Unsuspected Error Occurred",
                Toast.LENGTH_SHORT
            )
                .show()
        }

        val queue=Volley.newRequestQueue(this@OTPActivity)
        val url="http://13.235.250.119/v2/reset_password/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number",userNumber )
        jsonParams.put("password", menterPassword)
        jsonParams.put("otp", menterOtp)

        if (ConnectionManager().checkConnectivity(this@OTPActivity)) {

            val request =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    println("response $it")
                    try {
                        val dataRequest = it.getJSONObject("data")
                        val success = dataRequest.getBoolean("success")
                        if (success) {
                            val requestMessage = dataRequest.getBoolean("successMessage")
                                val builder = AlertDialog.Builder(this@OTPActivity)
                                builder.setTitle("Successful")
                                builder.setMessage("Your $requestMessage")
                                builder.setCancelable(false)
                                builder.setPositiveButton("Ok") { _, _ ->
                                    val intent = Intent(
                                        this@OTPActivity,
                                        LoginActivity::class.java
                                    )
                                    startActivity(intent)
                                }
                                builder.create().show()
                            } else {
                            Toast.makeText(
                                this@OTPActivity,
                                "Not getting true  Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@OTPActivity, "Some Error Occurred!!!!", Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {
                    Toast.makeText(
                        this@OTPActivity, "Getting Volley Error ", Toast.LENGTH_SHORT
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
            val dialog = AlertDialog.Builder(this@OTPActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Setting") { _, _ ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(this@OTPActivity)
            }
            dialog.create()
            dialog.show()

        }


    }
}