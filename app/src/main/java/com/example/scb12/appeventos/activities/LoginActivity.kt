package com.example.scb12.appeventos.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.scb12.appeventos.MainActivity
import com.example.scb12.appeventos.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        bLogin.setOnClickListener {
            //checkUser()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
       /* bRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::clas.java)
            startActivity(intent)
        }*/
    }

}
