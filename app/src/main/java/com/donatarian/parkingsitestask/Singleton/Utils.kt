package com.donatarian.parkingsitestask.Singleton

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.donatarian.parkingsitestask.Models.ParkingSite
import com.donatarian.parkingsitestask.Models.ParkingSiteEngine
import com.donatarian.parkingsitestask.retrofit.RetrofitCaller
import com.donatarian.parkingsitestask.retrofit.RetrofitClientInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Utils(var context: Context) {
    private lateinit var retrofitClientInterface: RetrofitClientInterface
    lateinit var call: Call<ParkingSiteEngine>

    // List to hold the data from JSON File.
    var parkingSiteList: MutableList<ParkingSite> = mutableListOf()

    // URL for Retrofit Call
      val URL = "http://192.168.0.121:88" //localhost IP Address.



    fun initializeList(list: List<ParkingSite>?) {
        parkingSiteList = list?.toMutableList() ?: mutableListOf()
    }

    fun getDataFromAPI(handler: android.os.Handler) {
        retrofitClientInterface = RetrofitCaller().createService()
        call = retrofitClientInterface.getParkingSites()

        call.enqueue(object : Callback<ParkingSiteEngine> {
            override fun onResponse(call: Call<ParkingSiteEngine>?,
                                    response: Response<ParkingSiteEngine>?) {
                if (response!!.isSuccessful) {
                    val list = response.body()?.parking_sites
                    initializeList(list)
                    handler.sendEmptyMessage(1)

                } else {
                    Toast.makeText(context,"",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ParkingSiteEngine>?, t: Throwable?) {
                Toast.makeText(context,t?.message ,Toast.LENGTH_SHORT).show()
            }
        })
    }


}
