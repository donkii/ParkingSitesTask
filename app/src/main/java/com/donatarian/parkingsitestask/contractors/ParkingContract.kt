package com.donatarian.parkingsitestask.contractors

import com.donatarian.parkingsitestask.models.ParkingSite
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

interface ParkingContract {

    interface Model{
        //No changes needs to be made on models
    }

    interface View{
        //used to initialize the list
        fun showList(list: List<ParkingSite>)
        //draws polyline on the map
        fun setPolyline(lineoption: PolylineOptions)
    }

    interface Presenter{
        //makes API call and gets the list from JSON
        fun loadList()
        //gets the url from google API
        fun getDirectionUrl(origin: LatLng, dest:LatLng): String
        //executes the async task
        fun executeTask(url: String)
    }
}