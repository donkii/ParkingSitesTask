package com.donatarian.parkingsitestask.Singleton

import android.util.Log
import com.donatarian.parkingsitestask.Models.ParkingSite
import com.donatarian.parkingsitestask.Models.ParkingSiteEngine
import com.donatarian.parkingsitestask.retrofit.RetrofitCaller
import com.donatarian.parkingsitestask.retrofit.RetrofitClientInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//class Utils private constructor() {
//
//    companion object {
//        //Debuggable field to check instance count
//        var myInstancesCount = 0;
//        private lateinit var mInstance: Utils
//
//        @Synchronized
//        fun getInstance(): Utils {
//            if(mInstance == null)
//                mInstance = Utils()
//            return mInstance
//        }
//    }
//
//}


class Utils {
    private lateinit var retrofitClientInterface: RetrofitClientInterface
    lateinit var call: Call<ParkingSiteEngine>

    // List to hold the data from JSON File.
    lateinit var parkingSiteList: List<ParkingSite>

    // URL for Retrofit Call
    private val URL = "http://192.168.0.121:88" //localhost IP Address.






    // Get URL Function
    fun getURL(): String {
        return this.URL
    }

    fun getDataFromAPI() {
        retrofitClientInterface = RetrofitCaller().createService()
        call = retrofitClientInterface.getParkingSites()

        call.enqueue(object : Callback<ParkingSiteEngine> {
            override fun onResponse(call: Call<ParkingSiteEngine>?,
                                    response: Response<ParkingSiteEngine>?) {
                if (response!!.isSuccessful) {
                    parkingSiteList = response.body()!!.parkingSites!!
                }
                else {
                    Log.e("gadad", "blablabla")
                }
            }

            override fun onFailure(call: Call<ParkingSiteEngine>?, t: Throwable?) {
                Log.e("failed", "blablabla")
            }
        })
    }










    // Creating the Singleton Instance
    companion object {

        private var instance: Utils? = null
        @Synchronized
        private fun createInstance() {
            if (instance == null) {
                instance = Utils()
            }
        }

        fun getInstance(): Utils? {
            if (instance == null) createInstance()
            return instance
        }
    }
}
