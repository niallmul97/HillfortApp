package views.editLocation

import com.example.hillfort.helpers.readImageFromPath
import com.example.hillfort.main.MainApp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_hillfort_maps.*

class EditLocationPresenter(val view: EditLocationView) {
    var app: MainApp

    init {
        app = view.application as MainApp
    }

    fun doConfigureMap(){
        view.map.uiSettings.setZoomControlsEnabled(true)
        app.users.findAllHillforts(app.currentUser).forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            view.map.addMarker(options).tag = it.id
            view.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
            view.map.setOnMarkerClickListener(view)
        }
    }

    fun doMarkerClick(marker: Marker): Boolean{
        view.textViewTitle.text = marker.title
        val hillfort = app.users.findHillfortById(app.currentUser, marker.tag.toString().toLong())
        if (hillfort != null){
            view.textViewDescription.text = hillfort.description
            view.imageView.setImageBitmap(readImageFromPath(view, hillfort.image[0]))
        }
        return false
    }
}