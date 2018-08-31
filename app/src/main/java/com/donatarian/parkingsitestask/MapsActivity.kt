package com.donatarian.parkingsitestask

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.donatarian.parkingsitestask.Models.ParkingSite
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import kotlinx.android.synthetic.main.activity_maps.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException


class MapsActivity : AppCompatActivity(), ParkingContract.View {

    lateinit var googleMap: GoogleMap
    lateinit var mapFragment: SupportMapFragment

    lateinit var presenter: ParkingContract.Presenter

    lateinit var parkingSitesList: List<ParkingSite>
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

        mapFragment.retainInstance = true
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

    override fun showList(list: List<ParkingSite>) {
        parkingSitesList = list
        var location: LatLng
//        for (parking in parkingSitesList){
//            location = LatLng(parking.location?.latitude!!, parking.location?.longitude!!)
//            googleMap.addMarker(MarkerOptions().position(location).title(parking.title)
//                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_parking_site )))
//        }
    }

    fun showRoute() {
        originLocation = getLocationFromAddress(txtOrigin.text.toString())
        destinationLocation = getLocationFromAddress(txtDestination.text.toString())

        distance = SphericalUtil.computeDistanceBetween(originLocation, destinationLocation)

        if (originLocation == LatLng(0.0, 0.0) || destinationLocation == LatLng(0.0, 0.0)) {
        } else {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(originLocation).title(txtDestination.text.toString()))
            googleMap.addMarker(MarkerOptions().position(destinationLocation).title(txtOrigin.text.toString()))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 15f))

            val URL = presenter.getDirectionUrl(originLocation, destinationLocation)
            presenter.executeTask(URL)
        }
    }

    fun showParkingSites() {
        doAsync {
            val searchIncrement = parkingSearchInterval(distance, lineOption.points.size, 50)

            for (parking in parkingSitesList) {
                var i = 0
                val parkingLatLng = LatLng(parking.location!!.latitude!!, parking.location!!.longitude!!)
                var distanceBetweenParkingAndPoint: Double
                while (i < lineOption.points.size) {
                        distanceBetweenParkingAndPoint = SphericalUtil.computeDistanceBetween(parkingLatLng, lineOption.points[i])
                        if (distanceBetweenParkingAndPoint < 100) {
                            uiThread {
                                googleMap.addMarker(MarkerOptions().position(parkingLatLng).title(parking.title)
                                        .icon(bitmapDescriptorFromVector(applicationContext, R.drawable.ic_parking_site)))
                            }
                        }
                        i += searchIncrement
                    }
                }
            }
        }

        fun parkingSearchInterval(distance: Double, points: Int, interval: Int): Int {
            val distancePerPoint = distance / points
            val pointsForInterval = interval / distancePerPoint

            return pointsForInterval.toInt()
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
    }

