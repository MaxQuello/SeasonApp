package com.example.seasonapp.model

data class RequestRegistration(val nome : String,val cognome:String,val gender:String,val dataNascita:String,
    val mail:String,val numeroTelefono:String,val username:String,val password:String,val domanda:String,val risposta:String,
    val codiceSconto : Int)
