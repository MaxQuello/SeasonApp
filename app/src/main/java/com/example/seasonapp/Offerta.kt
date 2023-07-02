package com.example.seasonapp

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate

class Offerta(val roomId:Int,val tipologiaCamere: String, val nCamere: Int, val prezzo: Double, val dataCheckIn: LocalDate?, val dataCheckOut: LocalDate?, val nOspiti: Int, val image : Bitmap? = null) : Parcelable{
    //card
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readDouble(),
        LocalDate.parse(parcel.readString()),
        LocalDate.parse(parcel.readString()),
        parcel.readInt(),
        parcel.readValue(Bitmap::class.java.classLoader) as Bitmap?
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(roomId)
        parcel.writeString(tipologiaCamere)
        parcel.writeInt(nCamere)
        parcel.writeDouble(prezzo)
        parcel.writeString(dataCheckIn.toString())
        parcel.writeString(dataCheckOut.toString())
        parcel.writeInt(nOspiti)
        parcel.writeParcelable(image, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Offerta> {
        override fun createFromParcel(parcel: Parcel): Offerta {
            return Offerta(parcel)
        }

        override fun newArray(size: Int): Array<Offerta?> {
            return arrayOfNulls(size)
        }
    }
}