package com.donatarian.parkingsitestask

import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.donatarian.parkingsitestask.Models.ParkingSite
import com.donatarian.parkingsitestask.Singleton.Utils
import com.donatarian.parkingsitestask.map.GoogleMapDTO

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_maps.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

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
        googleMap.addPolyline(lineoption)
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
            googleMap.addMarker(MarkerOptions().position(location).title(parking.title))
        }
    }

    fun showRoute(){
        val originLocation = getLocationFromAddress(txtDestination.text.toString())
        val destinationLocation = getLocationFromAddress(txtOrigin.text.toString())

        if (originLocation == LatLng(0.0, 0.0) || destinationLocation == LatLng(0.0, 0.0)){
        }else {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(originLocation).title(txtDestination.text.toString()))
            googleMap.addMarker(MarkerOptions().position(destinationLocation).title(txtOrigin.text.toString()))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 12f))

            val URL = presenter.getDirectionUrl(originLocation, destinationLocation)
            presenter.executeTask(URL)
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
}
