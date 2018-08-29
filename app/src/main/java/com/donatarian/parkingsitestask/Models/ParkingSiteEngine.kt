package com.donatarian.parkingsitestask.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ParkingSiteEngine {

    @SerializedName("parking_sites")
    @Expose
    var parkingSites: List<ParkingSite>? = null

}