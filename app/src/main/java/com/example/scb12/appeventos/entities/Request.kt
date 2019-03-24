package com.example.scb12.appeventos.entities

import android.util.Log
import java.net.URL

class Request(val url: String) {
    fun run() {
        val tiempoJsonStr = URL(url).readText()
        Log.d("MIO", tiempoJsonStr)
    }
}