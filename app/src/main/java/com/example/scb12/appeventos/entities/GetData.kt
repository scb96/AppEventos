package com.example.scb12.appeventos.entities

import io.reactivex.Observable
import retrofit2.http.GET
import java.util.*
import kotlin.collections.ArrayList

interface GetData {

    @GET("events/search/?search_type=name&token=EGCNBKQRZWJAAPOFDFVJ&")
    fun getData() : Observable<List<Event>>
}