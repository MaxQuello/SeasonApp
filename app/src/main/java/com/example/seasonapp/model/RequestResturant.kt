package com.example.seasonapp.model

import java.time.LocalDate

data class RequestResturant(val idUtente:Int,val numberOfGuest:Int,val resturantDate: LocalDate,val scelta:String)
