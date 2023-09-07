package com.example.foodapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
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

class ForgetPassActivity : AppCompatActivity() {
    private lateinit var enterForgetPhone:EditText
    private lateinit var enterForgetEmail:EditText
    private lateinit var forgetbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        enterForgetPhone=findViewById(R.id.et_forget_phone)
        enterForgetEmail=findViewById(R.id.et_forget_email)
        forgetbtn=findViewById(R.id.btnForgot)

        forgetbtn.setOnClickListener{
            val menterForgetPhone=enterForgetPhone.text.toString()
            val menterForgetEmail=enterForgetEmail.text.toString()
            if(validateInput(menterForgetPhone,menterForgetEmail)){
                sendOTP(menterForgetPhone,menterForgetEmail)
            }
        }
    }
    private fun validateInput(menterForgetPhone:String, menterForgetEmail:String):Boolean{
        if (menterForgetPhone.length!=10){
            Toast.makeText(this@ForgetPassActivity, "Enter Valid Number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (menterForgetEmail.isEmpty()){
            Toast.makeText(this@ForgetPassActivity, "Enter Valid Email", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun sendOTP(menterForgetPhone: String, menterForgetEmail: String){
        val queue=Volley.newRequestQueue(this@ForgetPassActivity)
        val url=" http://13.235.250.119/v2/forgot_password/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number",menterForgetPhone)
        jsonParams.put("email", menterForgetEmail)

        if (ConnectionManager().checkConnectivity(this@ForgetPassActivity)) {

            val request =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    println("response $it")
                    try {
                        val dataRequest = it.getJSONObject("data")
                        val success = dataRequest.getBoolean("success")
                        if (success) {
                            val firstTry = dataRequest.getBoolean("first_try")
                            if (firstTry) {
                                val builder = AlertDialog.Builder(this@ForgetPassActivity)
                                builder.setTitle("Information")
                                builder.setMessage("Please check your registered Email for the OTP.")
                                builder.setCancelable(false)
                                builder.setPositiveButton("Ok") { _, _ ->
                                    val intent = Intent(
                                        this@ForgetPassActivity,
                                        OTPActivity::class.java
                                    )
                                    intent.putExtra("mobile_number", menterForgetPhone)
                                    startActivity(intent)
                                }
                                builder.create().show()
                            }
                            else{
                                val builder = AlertDialog.Builder(this@ForgetPassActivity)
                                builder.setTitle("Information")
                                builder.setMessage("Please refer to the previous email for the OTP.")
                                builder.setCancelable(false)
                                builder.setPositiveButton("Ok") { _, _ ->
                                    val intent = Intent(
                                        this@ForgetPassActivity,
                                        OTPActivity::class.java
                                    )
                                    intent.putExtra("mobile_number", menterForgetPhone)
                                    startActivity(intent)
                                }
                                builder.create().show()
                            }
                        } else {
                            Toast.makeText(
                                this@ForgetPassActivity,
                                "Not getting true  Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ForgetPassActivity, "Some Error Occurred!!!!", Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {
                    Toast.makeText(
                        this@ForgetPassActivity, "Getting Volley Error ", Toast.LENGTH_SHORT
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
            val dialog = AlertDialog.Builder(this@ForgetPassActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Setting") { _, _ ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(this@ForgetPassActivity)
            }
            dialog.create()
            dialog.show()

        }
    }
}
