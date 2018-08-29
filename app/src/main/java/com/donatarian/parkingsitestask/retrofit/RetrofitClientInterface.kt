package com.donatarian.parkingsitestask.retrofit

import com.donatarian.parkingsitestask.Models.ParkingSiteEngine
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitClientInterface {


    @GET("/parking_sites.json")
    fun getParkingSites(): Call<ParkingSiteEngine>



}