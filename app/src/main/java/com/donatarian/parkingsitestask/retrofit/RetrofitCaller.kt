package com.donatarian.parkingsitestask.retrofit

import com.donatarian.parkingsitestask.Singleton.Utils
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitCaller {


    private var okHttpClient = OkHttpClient()
    private var objectMapper = ObjectMapper()
    private var retrofit = Retrofit.Builder()
            .baseUrl(Utils.getInstance()!!.getURL())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()


    fun createService(): RetrofitClientInterface {
        return retrofit.create(RetrofitClientInterface::class.java)
    }

}