package com.example.seasonapp.model

import java.time.LocalDate

data class RequestPrenotaCamera(val roomId: Int, val dataCheckIn : LocalDate,val dataCheckOut : LocalDate,val refUtente:Int)
