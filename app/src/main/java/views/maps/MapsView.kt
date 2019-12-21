package views.maps

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hillfort.R
import com.example.hillfort.helpers.readImageFromPath
import com.example.hillfort.models.HillfortModel
import com.example.hillfort.models.Location

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import views.Base.BaseView
import views.hillfortList.HillfortListPresenter

class MapsView : BaseView(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: MapsPresenter
    lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        super.init(toolbar)

        presenter = initPresenter (MapsPresenter(this)) as MapsPresenter

        mapView2.onCreate(savedInstanceState);
        mapView2.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadPlacemarks()
        }
    }

    override fun showHillfort(hillfort: HillfortModel) {
        textViewTitle.text = hillfort.title
        textViewDescription.text = hillfort.description
        imageView.setImageBitmap(readImageFromPath(this, hillfort.image[0]))
    }

    override fun showHillforts(hillforts: List<HillfortModel>) {
        presenter.doPopulateMap(map, hillforts)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
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
}