package com.example.hillfort.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.example.hillfort.R
import com.example.hillfort.main.MainApp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*

class HillfortMapsActivity : AppCompatActivity() {

    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        app = application as MainApp
        toolbar.title = title
        setSupportActionBar(toolbar)
        mapView2.onCreate(savedInstanceState);
        mapView2.getMapAsync {
            map = it
            configureMap()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView2.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView2.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView2.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView2.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView2.onSaveInstanceState(outState)
    }

    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        app.users.findAllHillforts(app.currentUser).forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }
}
