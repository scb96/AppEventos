package com.example.scb12.appeventos.adapters

//import androidx.lifecycle.ViewModel
import android.arch.lifecycle.ViewModel
import com.example.scb12.appeventos.entities.Event

class FavsListViewModel: ViewModel(){
    var favsList: ArrayList<Event> = ArrayList()
}