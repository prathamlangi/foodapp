package com.example.foodapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    private lateinit var enterPhone: EditText
    private lateinit var enterPass:EditText
    private lateinit var btnLogin: Button
    private lateinit var txtForgetPass: TextView
    private lateinit var txtRegister:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        enterPhone=findViewById(R.id.et_phone)
        enterPass=findViewById(R.id.et_password)
        btnLogin=findViewById(R.id.btn_login)
        txtForgetPass=findViewById(R.id.txt_forget_pass)
        txtRegister=findViewById(R.id.txt_register)

        txtForgetPass.setOnClickListener{
            val intent= Intent(this@LoginActivity,ForgetPassActivity::class.java)
            startActivity(intent)
        }
        txtRegister.setOnClickListener{
            val intent=Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}