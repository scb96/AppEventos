package com.example.scb12.appeventos.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.scb12.appeventos.MainActivity
import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.database.SQLiteHelperConection
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception

class DealActivity : AppCompatActivity() {

    private lateinit var compositeDisposable: CompositeDisposable
    lateinit var logged: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)

        compositeDisposable = CompositeDisposable()

        val conn: SQLiteHelperConection = SQLiteHelperConection(this, "bd_appEventos", null, 1)

    }

    override fun onPostResume() {
        super.onPostResume()
        val prefs = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val log = prefs.getBoolean("LOGIN", false)
        val background = object : Thread(){
            override fun run() {
                try {
                    Thread.sleep(3000)

                    if(log) {
                        val intent = Intent(baseContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val loginIntent = Intent(baseContext, LoginActivity::class.java)
                        startActivity(loginIntent)
                        finish()
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
       /* if(!::logged.isInitialized || logged == "") {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)

        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


        }

    }*/
}
