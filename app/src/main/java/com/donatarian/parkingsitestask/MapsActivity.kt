package com.donatarian.parkingsitestask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.donatarian.parkingsitestask.Models.ParkingSite
import com.donatarian.parkingsitestask.Singleton.Utils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, ParkingContract.View {

    @Inject
    lateinit var utils : Utils
    private lateinit var mMap: GoogleMap
    lateinit var parkingSitesList: List<ParkingSite>
    lateinit var presenter: ParkingContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        App.appComponent.inject(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        presenter = ParkingPresenter(this)
        presenter.loadList()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


    }

    override fun showList(list: List<ParkingSite>) {
        parkingSitesList = list

        var park: ParkingSite = parkingSitesList.get(0)
        var location: LatLng = LatLng(park.location?.latitude!!, park.location?.longitude!!)
        val zoomLevel = 15.0f

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))

        for (parking in parkingSitesList){
            location = LatLng(parking.location?.latitude!!, parking.location?.longitude!!)
            mMap.addMarker(MarkerOptions().position(location).title(parking.title))
        }
    }
}
