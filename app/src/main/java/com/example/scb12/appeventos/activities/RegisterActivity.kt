package com.example.scb12.appeventos.activities

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.Utils.Utils
import com.example.scb12.appeventos.database.SQLiteHelperConection
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var name: String
    private lateinit var lastName: String
    private lateinit var userName: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        bRegister.setOnClickListener {
            name = etName.text.toString()
            lastName = etLastName.text.toString()
            userName = etUsername.text.toString()
            password = etPassword.text.toString()

            registerUser()
        }
    }

    private fun registerUser() {
        val conn: SQLiteHelperConection = SQLiteHelperConection(this, "bd_appEventos", null, 1)

        val db = conn.writableDatabase

        val values = ContentValues()
        values.put(Utils().NAME_FIELD, name)
        values.put(Utils().LASTNAME_FIELD, lastName)
        values.put(Utils().USERNAME_FIELD, userName)
        values.put(Utils().PASSWORD_FIELD, password)

        val res = db.insert(Utils().TABLE_USERS, Utils().USERNAME_FIELD, values)
        Toast.makeText(this, "USUARIO " + res + "REGISTRADO", Toast.LENGTH_LONG).show()
        db.close()
        finish()

    }
}
