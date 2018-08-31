package com.donatarian.parkingsitestask.models

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