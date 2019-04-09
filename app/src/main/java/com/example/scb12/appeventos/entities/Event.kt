package com.example.scb12.appeventos.entities

import java.io.FileDescriptor

data class Event(var id: String,
                 var name: String,
                 var startDate: String,
                 var finishDate: String,
                 var url: String,
                 var logoId: String,
                 var logoUrl: String,
                 var venueId: String,
                 var categoryId: String,
                 var isFree: String,
                 var description: String,
                 var isFav: Boolean
                 )