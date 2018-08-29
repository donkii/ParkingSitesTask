package com.donatarian.parkingsitestask.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Location {

    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null
    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null

}