package com.example.scb12.appeventos.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.scb12.appeventos.Utils.Utils

class SQLiteHelperConection: SQLiteOpenHelper {

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
            : super(context, name, factory, version)

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(Utils().CREATE_TABLE_USERS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
