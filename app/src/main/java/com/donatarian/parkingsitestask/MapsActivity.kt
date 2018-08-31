package com.donatarian.parkingsitestask

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.donatarian.parkingsitestask.Models.ParkingSite
import com.donatarian.parkingsitestask.Singleton.Utils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException
import javax.inject.Inject
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.support.annotation.DrawableRes
import com.google.android.gms.maps.model.BitmapDescriptor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MapsActivity : AppCompatActivity(), ParkingContract.View {

    @Inject
    lateinit var utils : Utils

    lateinit var googleMap: GoogleMap
    lateinit var mapFragment : SupportMapFragment

    private lateinit var mMap: GoogleMap
    lateinit var parkingSitesList: List<ParkingSite>
    lateinit var presenter: ParkingContract.Presenter
    internal var markerPoints = ArrayList<LatLng>()
    private lateinit var parkingList: MutableList<ParkingSite>
    private var parkingLocations: MutableList<LatLng> = mutableListOf()
    lateinit var URL: String
    lateinit var lineOption: PolylineOptions
    lateinit var originLocation: LatLng
    lateinit var destinationLocation: LatLng
    var distance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        App.appComponent.inject(this)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
        val initialLocation = LatLng(48.210033, 16.363449)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10.5f))
        }

        presenter = ParkingPresenter(this)
        presenter.loadList()

        btnSearch.setOnClickListener {
            showRoute()

        }
    }

    override fun getPolyline(lineoption: PolylineOptions) {
        lineOption = lineoption
        googleMap.addPolyline(lineoption)
        showParkingSites()
    }


//region cde
//    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
//        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
//            val client = OkHttpClient()
//            val request = Request.Builder().url(url).build()
//            val response = client.newCall(request).execute()
//            val data = response.body()!!.string()
//            Log.d("GoogleMap" , " data : $data")
//            val result =  ArrayList<List<LatLng>>()
//            try{
//                val respObj = Gson().fromJson(data, GoogleMapDTO::class.java)
//
//                val path =  ArrayList<LatLng>()
//
//                for (i in 0..(respObj.routes[0].legs[0].steps.size-1)){
//                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
//                }
//                result.add(path)
//            }catch (e:Exception){
//                e.printStackTrace()
//            }
//            return result
//        }
//
//        override fun onPostExecute(result: List<List<LatLng>>) {
//            val lineoption = PolylineOptions()
//            for (i in result.indices){
//                lineoption.addAll(result[i])
//                lineoption.width(10f)
//                lineoption.color(Color.BLUE)
//                lineoption.geodesic(true)
//            }
//            googleMap.addPolyline(lineoption)
//        }
//    }
//
//    fun decodePolyline(encoded: String): List<LatLng> {
//
//        val poly = ArrayList<LatLng>()
//        var index = 0
//        val len = encoded.length
//        var lat = 0
//        var lng = 0
//
//        while (index < len) {
//            var b: Int
//            var shift = 0
//            var result = 0
//            do {
//                b = encoded[index++].toInt() - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lat += dlat
//
//            shift = 0
//            result = 0
//            do {
//                b = encoded[index++].toInt() - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lng += dlng
//
//            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
//            poly.add(latLng)
//        }
//
//        return poly
//    }
//endregion.

//region abc.
//        var polylineOptions =
//                PolylineOptions().color(Color.RED).width(5f).add(startingLocatin).add(endingLocatin)
//        mMap.clear()
//        mMap.addPolyline(polylineOptions)

//        utils.getDataFromAPI(Handler {
//
//            if (it.what == 1) {
//                var location: LatLng
//
//                for (parking in parkingList){
//                    location = LatLng(parking.location?.latitude!!, parking.location?.longitude!!)
//                    mMap.addMarker(MarkerOptions().position(location).title(parking.title))
//                }
//            }
//
//            true
//        })
//        utils.getDataFromAPI(Handler {
//
//            if (it.what == 1) {
//
//                var location: LatLng = startingLocatin
//                parkingList = utils.parkingSiteList
//                for (parking in parkingList){
//                    location = LatLng(parking.location?.latitude!!, parking.location?.longitude!!)
//                    parkingLocations.add(location)
//
//                }
//                var polylineOptions =
//                        PolylineOptions().color(Color.RED).width(5f).add(location)
//                googleMap.addPolyline(polylineOptions)
//            }
//
//            true
//        })
//endregion.

    override fun showList(list: List<ParkingSite>) {
        parkingSitesList = list
        var location: LatLng
        for (parking in parkingSitesList){
            location = LatLng(parking.location?.latitude!!, parking.location?.longitude!!)
            googleMap.addMarker(MarkerOptions().position(location).title(parking.title)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_parking_site )))
        }
    }

    fun showRoute(){
        originLocation = getLocationFromAddress(txtOrigin.text.toString())
        destinationLocation = getLocationFromAddress(txtDestination.text.toString())

        distance = distanceInMeters(originLocation.latitude, originLocation.longitude,
                destinationLocation.latitude, destinationLocation.longitude)

        if (originLocation == LatLng(0.0, 0.0) || destinationLocation == LatLng(0.0, 0.0)){
        }else {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(originLocation)
                    .title(txtDestination.text.toString()))
            googleMap.addMarker(MarkerOptions().position(destinationLocation)
                    .title(txtOrigin.text.toString()))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 12f))

            val URL = presenter.getDirectionUrl(originLocation, destinationLocation)
            presenter.executeTask(URL)
        }
    }

    fun showParkingSites() {
        doAsync {
            var x = distance / lineOption.points.size
            var y = 50/x
            var z = y.toInt()

            for (parking in parkingSitesList) {
                var i = 0
                while (i < lineOption.points.size) {
                    var location: LatLng
                    if (distanceInMeters(parking.location!!.latitude!!, parking.location!!.longitude!!,
                                    lineOption.points[i].latitude, lineOption.points[i].longitude) < 100) {
                        location = LatLng(parking.location?.latitude!!, parking.location?.longitude!!)
                        uiThread {
                            googleMap.addMarker(MarkerOptions().position(location).title(parking.title)
                                    .icon(bitmapDescriptorFromVector(applicationContext, R.drawable.ic_parking_site)))
                        }

                    }
                    i += z
                }
            }
        }
    }

    fun getLocationFromAddress(strAddress: String): LatLng {

        var point = LatLng(0.0, 0.0)
        if (strAddress.trim { it <= ' ' } != "") {
            val coder = Geocoder(applicationContext)
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

    private fun bitmapDescriptorFromVector
            (context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor {
        val background = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight)
        val bitmap =
                Bitmap.createBitmap(background.intrinsicWidth, background.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun distanceInMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double{
        val R = 6378.137
        val dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180
        val dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180
        val a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                Math.sin(dLon/2) * Math.sin(dLon/2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
        val d = R * c
        return d * 1000 // meters
    }
}
