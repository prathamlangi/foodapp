package com.example.foodapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ForgetPassActivity : AppCompatActivity() {

    private lateinit var forgetbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        forgetbtn=findViewById(R.id.btnForgot)

        forgetbtn.setOnClickListener{
            val forget= Intent(this@ForgetPassActivity,OTPActivity::class.java)
            startActivity(forget)
        }
    }
}