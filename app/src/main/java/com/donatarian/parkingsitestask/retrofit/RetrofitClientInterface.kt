package com.donatarian.parkingsitestask.retrofit

import com.donatarian.parkingsitestask.models.ParkingSiteEngine
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitClientInterface {


    @GET("/parking_sites.json")
    fun getParkingSites(): Call<ParkingSiteEngine>



}