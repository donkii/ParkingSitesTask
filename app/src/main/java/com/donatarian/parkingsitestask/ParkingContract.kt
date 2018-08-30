package com.donatarian.parkingsitestask

import com.donatarian.parkingsitestask.Models.ParkingSite
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

interface ParkingContract {

    interface Model{

    }

    interface View{
        fun showList(list: List<ParkingSite>)
        fun getPolyline(lineoption: PolylineOptions)
    }

    interface Presenter{
        fun loadList()
        fun getDirectionUrl(origin: LatLng, dest:LatLng): String
        fun executeTask(url: String)
    }
}