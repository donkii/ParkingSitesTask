package com.donatarian.parkingsitestask.retrofit

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitCaller {


    private var okHttpClient = OkHttpClient()
    private var objectMapper = ObjectMapper()
    private var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.121:88")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()


    fun createService(): RetrofitClientInterface {
        return retrofit.create(RetrofitClientInterface::class.java)
    }

}