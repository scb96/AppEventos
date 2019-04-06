package com.example.scb12.appeventos.activities

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.scb12.appeventos.MainActivity
import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.Utils.Utils
import com.example.scb12.appeventos.database.SQLiteHelperConection
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.internal.Util

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        bLogin.setOnClickListener {
            checkUser()
           /* */
        }
        bRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkUser() {
        val conn = SQLiteHelperConection(this, "bd_appEventos", null, 1)
        val db = conn.readableDatabase
        val fields = arrayOf(Utils().USERNAME_FIELD, Utils().PASSWORD_FIELD, Utils().NAME_FIELD, Utils().LASTNAME_FIELD)
        val parameters = arrayOf(etUsername.text.toString())

        try {
            val cursor = db.query(
                Utils().TABLE_USERS,
                fields,
                Utils().USERNAME_FIELD + "=?",
                parameters,
                null,
                null,
                null
            )
            cursor.moveToFirst()

            if(etUsername.text.toString() == cursor.getString(0)) {
                if(etPassword.text.toString() == cursor.getString(1)) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("NAME", cursor.getString(2))
                    intent.putExtra("LASTNAME", cursor.getString(3))
                    intent.putExtra("USERNAME", cursor.getString(0))
                    startActivity(intent)
                    cursor.close()
                } else {
                    cursor.close()
                    Toast.makeText(this, R.string.password_fail, Toast.LENGTH_LONG).show()
                }
            } else {
                cursor.close()
                Toast.makeText(this, R.string.username_fail, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, R.string.user_not_exists, Toast.LENGTH_LONG).show()

        }
    }
}
