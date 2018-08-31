package com.donatarian.parkingsitestask.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ParkingSiteEngine {

    @SerializedName("parking_sites")
    @Expose
    var parking_sites: List<ParkingSite>? = null



}