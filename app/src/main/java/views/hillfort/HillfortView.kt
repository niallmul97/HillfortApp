package views.hillfort

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import com.bumptech.glide.Glide
import com.example.hillfort.R
import com.example.hillfort.helpers.readImageFromPath
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import com.example.hillfort.models.HillfortModel
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_hillfort.hillFortVisited
import kotlinx.android.synthetic.main.activity_hillfort.hillfortDescription
import kotlinx.android.synthetic.main.activity_hillfort.hillfortTitle
import kotlinx.android.synthetic.main.content_bottom_nav.*
import views.Base.BaseView
import views.BottomNavPresenter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HillfortView :  BaseView(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var map: GoogleMap
    lateinit var presenter: HillfortPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)

        //Sets up toolbar
        super.init(toolbarAdd, true)

        info("Hillfort Activity started..")
        presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter

        //when a user clicks the image
        hillfortImage.setOnClickListener {presenter.doIterateImage()}

        //when the delete image button is pressed
        btnDeleteImage.setOnClickListener {presenter.doRemoveImage()}

        //when the add image button is clicked, opens the image picker
        chooseImage.setOnClickListener {presenter.doSelectImage()}

        //if the visited box is checked
        hillFortVisited.setOnClickListener{presenter.doHillfortVisited()}

        //if favourites is checked
        favourites.setOnClickListener{presenter.doFavourites()}

        hillFortLocationMap.onCreate(savedInstanceState);
        hillFortLocationMap.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }

        }
    }

    override fun showHillfort(hillfort: HillfortModel){
        hillfortTitle.setText(hillfort.title)
        hillfortDescription.setText(hillfort.description)
        hillFortDateVisited.setText(hillfort.dateVisited)
        ratingBar.rating = hillfort.rating.toFloat()
        var strLocation = "Latitude: " + hillfort.location.lat.toString() + "\nLongitude: " +hillfort.location.lng.toString() + "\nZoom: " +hillfort.location.zoom.toString()
        notes.setText(hillfort.notes)
        if (hillfort.favourite){
            favourites.isChecked
        }
        if (hillfort.visited){
            hillFortVisited.isChecked
            hillFortDateVisited.visibility = VISIBLE
        }else {
            !hillFortVisited.isChecked
            hillFortDateVisited.visibility = INVISIBLE
        }
        if(hillfort.image.size != 0){
            Glide.with(this).load(hillfort.image[0]).into(hillfortImage);
            hillfortImage.visibility = VISIBLE
        }else {
            hillfortImage.visibility = GONE
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {

        //if the user is editing an existing hillfort, the delete icon will appear
        if (intent.hasExtra("hillfort_edit")) {
            menu.setGroupVisible(R.id.menuGroup, true)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            //when the cancel button is pressed, go back an activity, progress is not saved
            R.id.item_cancel -> {
                presenter.doCancel()
            }

            //when the delete button is pressed, the hillfort is deleted and the user is brought back and activity
            R.id.hillfort_delete -> {
                presenter.doDelete()
            }

            R.id.item_save ->{

                var checkDate = false
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH )

                if (hillfortTitle.text.isEmpty()) {
                    toast(R.string.enter_hillfort_title)
                }
                //checks if hillfort has been visited
                else if (hillFortVisited.isChecked){
                    val date = hillFortDateVisited.text.toString()

                    //validates the "date" as an actual date
                    try {
                        formatter.parse(date)
                        checkDate = true

                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                    //if validation is successful the "date" is set as the date visited
                    if (checkDate){
                        hillfort.dateVisited = date
                        presenter.doAddOrSave(hillfortTitle.text.toString(), hillfortDescription.text.toString(), notes.text.toString(), hillFortDateVisited.text.toString(), ratingBar.rating.toDouble(), favourites.isChecked.toString().toBoolean())
                    } else
                    //otherwise add a valid date
                        toast("Please enter a valid date")
                } else{
                    //if hillfort isn't visited, date defaults to "" and text field is gone
                    hillFortDateVisited.visibility = GONE
                    hillfort.dateVisited = ""
                    presenter.doAddOrSave(hillfortTitle.text.toString(), hillfortDescription.text.toString(), notes.text.toString(), hillFortDateVisited.text.toString(), ratingBar.rating.toDouble(), favourites.isChecked.toString().toBoolean())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        hillFortLocationMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        hillFortLocationMap.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        hillFortLocationMap.onPause()
    }

    override fun onResume() {
        super.onResume()
        hillFortLocationMap.onResume()
        presenter.doResartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        hillFortLocationMap.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!= null){
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }
}