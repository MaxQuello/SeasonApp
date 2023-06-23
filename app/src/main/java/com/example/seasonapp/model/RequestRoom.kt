package com.example.seasonapp.model

import java.time.LocalDate
import java.util.Date

data class RequestRoom(val checkInDate: LocalDate, val checkOutDate: LocalDate, val numberOfGuest:Int,val numberOfRoom : Int)
