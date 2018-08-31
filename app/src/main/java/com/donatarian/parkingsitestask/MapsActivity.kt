package com.donatarian.parkingsitestask

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.donatarian.parkingsitestask.contractors.ParkingContract
import com.donatarian.parkingsitestask.functions.Functions
import com.donatarian.parkingsitestask.models.ParkingSite
import com.donatarian.parkingsitestask.presenters.ParkingPresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.SphericalUtil
import kotlinx.android.synthetic.main.activity_maps.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MapsActivity : AppCompatActivity(), ParkingContract.View {

    lateinit var googleMap: GoogleMap
    lateinit var mapFragment: SupportMapFragment
    lateinit var presenter: ParkingContract.Presenter
    lateinit var parkingSitesList: List<ParkingSite>
    lateinit var lineOption: PolylineOptions
    lateinit var originLocation: LatLng
    lateinit var destinationLocation: LatLng
    var distance: Double = 0.0
    lateinit var parkingIcon: BitmapDescriptor
    val SEARCH_INTERVAL = 50
    val SEARCH_DISTANCE = 100
    val INITIAL_LOCATION = LatLng(48.210033, 16.363449)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        App.appComponent.inject(this)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(INITIAL_LOCATION, 15f))
        }

        parkingIcon = Functions.bitmapDescriptorFromVector(applicationContext,
                R.drawable.ic_parking_site)

        mapFragment.retainInstance = true
        presenter = ParkingPresenter(this)
        presenter.loadList()

        setupBtnClickListener()
    }

    //handles search button click
    fun setupBtnClickListener() {
        btnSearch.setOnClickListener {
            Functions.hide_keyboard(this, btnSearch)
            showRoute()
        }
    }

    override fun setPolyline(lineoption: PolylineOptions) {
        lineOption = lineoption
        googleMap.addPolyline(lineoption)
        showParkingSites()
    }

    override fun showList(list: List<ParkingSite>) {
        parkingSitesList = list
        var location: LatLng
        for (parking in parkingSitesList) {
            location = LatLng(parking.location?.latitude!!, parking.location?.longitude!!)
            googleMap.addMarker(MarkerOptions().position(location).title(parking.title)
                    .icon(parkingIcon))
        }
    }

    //Draws the markers from origin to destination and gets polyline
    fun showRoute() {
        originLocation = Functions.getLocationFromAddress(txtOrigin.text.toString(), this)
        destinationLocation = Functions.getLocationFromAddress(txtDestination.text.toString(), this)

        distance = SphericalUtil.computeDistanceBetween(originLocation, destinationLocation)

        if (originLocation == LatLng(0.0, 0.0) || destinationLocation == LatLng(0.0, 0.0)) {
            Toast.makeText(this, getString(R.string.address_not_found), Toast.LENGTH_SHORT).show()
        } else {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(originLocation).title(txtDestination.text.toString()))
            googleMap.addMarker(MarkerOptions().position(destinationLocation).title(txtOrigin.text.toString()))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 15f))

            val URL = presenter.getDirectionUrl(originLocation, destinationLocation)
            presenter.executeTask(URL)
        }
    }

    //Draws the parking markers near road and updates UI thread
    fun showParkingSites() {
        doAsync {
            val searchIncrement = Functions.parkingSearchInterval(distance,
                    lineOption.points.size, SEARCH_INTERVAL)

            for (parking in parkingSitesList) {
                var point = 0
                val parkingLatLng = LatLng(parking.location!!.latitude!!, parking.location!!.longitude!!)
                var distanceBetweenParkingAndPoint: Double
                while (point < lineOption.points.size) {
                    distanceBetweenParkingAndPoint = SphericalUtil.computeDistanceBetween(parkingLatLng, lineOption.points[point])
                    if (distanceBetweenParkingAndPoint < SEARCH_DISTANCE) {
                        uiThread {
                            googleMap.addMarker(MarkerOptions().position(parkingLatLng).title(parking.title)
                                    .icon(parkingIcon))
                        }
                    }
                    point += searchIncrement
                }
            }
        }
    }
}

