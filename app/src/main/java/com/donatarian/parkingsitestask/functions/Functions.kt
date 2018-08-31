package com.donatarian.parkingsitestask.functions

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import kotlin.math.roundToInt

class Functions {
    companion object {

        //Hides the keyboard
        fun hide_keyboard(activity: Activity, view: View) {
            try {
                val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken,
                        InputMethodManager.RESULT_UNCHANGED_SHOWN)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        //Converts vector drawable into bitmap
        fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor {
            val background = ContextCompat.getDrawable(context, vectorDrawableResourceId)
            background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
            val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
            vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight)
            val bitmap =
                    Bitmap.createBitmap(background.intrinsicWidth,
                            background.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            background.draw(canvas)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        //calculates in what interval will the search be made
        fun parkingSearchInterval(distance: Double, points: Int, interval: Int): Int {
            val distancePerPoint = distance / points
            val pointsForInterval = interval / distancePerPoint

            return pointsForInterval.roundToInt()
        }

        //converts address from google resourses to location in lat-lng
        fun getLocationFromAddress(strAddress: String, context: Context): LatLng {
            var point = LatLng(0.0, 0.0)
            if (strAddress.trim { it <= ' ' } != "") {
                val coder = Geocoder(context)
                val address: List<Address>

                try {
                    // May throw an IOException
                    address = coder.getFromLocationName(strAddress, 5)
                    if (address.isEmpty()) {
                        return point
                    }
                    val location = address[0]
                    location.latitude
                    location.longitude

                    point = LatLng(location.latitude, location.longitude)

                } catch (ex: IOException) {
                    ex.printStackTrace()
                }

            }
            return point
        }

    }
}