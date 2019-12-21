package views.maps

import android.app.Activity
import android.content.Intent
import com.example.hillfort.models.HillfortModel
import com.example.hillfort.models.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import views.Base.BasePresenter
import views.Base.BaseView

class MapsPresenter(view: BaseView) : BasePresenter(view) {

    fun doPopulateMap(map: GoogleMap, hillforts: List<HillfortModel>) {
        map.uiSettings.setZoomControlsEnabled(true)
        hillforts.forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }

    fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
        val hillforts = app.users.findHillfortById(app.currentUser, tag)
        if (hillforts != null) view?.showHillfort(hillforts)
    }

    fun loadPlacemarks() {
        view?.showHillforts(app.users.findAllHillforts(app.currentUser))
    }
}