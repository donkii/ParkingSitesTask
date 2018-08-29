package com.donatarian.parkingsitestask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.donatarian.parkingsitestask.Singleton.Utils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var utils : Utils

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        App.appComponent.inject(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        utils.getDataFromAPI(Handler {

            if (it.what == 1) {
                val result = utils.parkingSiteList.size.toString()
                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
            }

            true
        })

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.addMarker(MarkerOptions().position(LatLng(48.240514, 16.436456)).title("Marker in Sydney"))
        mMap.addMarker(MarkerOptions().position(LatLng(47.2358948689, 9.59410607815)).title("Marker in Sydney"))
        mMap.addMarker(MarkerOptions().position(LatLng(9.59663540125, 47.2386520491)).title("Marker in Sydney"))
        mMap.addMarker(MarkerOptions().position(LatLng(9.59792822599, 47.2352356012)).title("Marker in Sydney"))
        mMap.addMarker(MarkerOptions().position(LatLng(9.59797, 47.25577)).title("Marker in Sydney"))
        mMap.addMarker(MarkerOptions().position(LatLng(9.602207, 47.239259)).title("Marker in Sydney"))
        mMap.addMarker(MarkerOptions().position(LatLng(9.62073, 47.255551)).title("Marker in Sydney"))
        mMap.addMarker(MarkerOptions().position(LatLng(9.624301, 47.309196)).title("Marker in Sydney"))
        mMap.addMarker(MarkerOptions().position(LatLng(9.6352395, 47.2939685)).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(47.25577, 9.59797)))


    }

}
