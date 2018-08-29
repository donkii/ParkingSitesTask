package com.donatarian.parkingsitestask.Models

import android.location.Location
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ParkingSite {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("location")
    @Expose
    var location: Location? = null

}