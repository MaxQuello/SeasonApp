package com.example.seasonapp.api

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface UserAPI {

    @POST("postSelect/")
    @FormUrlEncoded
    fun login(@Field("query") query: String): Call<JsonObject>

    @POST("postSelect/")
    @FormUrlEncoded
    fun getIdUtente(@Field("query") query: String): Call<JsonObject>


    @POST("postSelect/")
    @FormUrlEncoded
    fun getAvaibleRooms(@Field("query") query: String) : Call<JsonObject>

    @POST("postInsert/")
    @FormUrlEncoded
    fun insertResturantReservation(@Field("query") query: String) : Call<JsonObject>

    @POST("postInsert/")
    @FormUrlEncoded
    fun insertUser(@Field("query") query: String) : Call<JsonObject>

    @POST("postUpdate/")
    @FormUrlEncoded
    fun modifica(@Field("query") query: String): Call<JsonObject>

    @GET
    fun getAvatar(@Url url: String) : Call<ResponseBody>

}