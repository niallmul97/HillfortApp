package com.example.hillfort.activities

import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.hillfort.R
import com.example.hillfort.helpers.readImage
import com.example.hillfort.helpers.readImageFromPath
import com.example.hillfort.helpers.showImagePicker
import com.example.hillfort.main.MainApp
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import com.example.hillfort.models.HillfortModel
import com.example.hillfort.models.Location
import kotlinx.android.synthetic.main.activity_hillfort.hillFortLocationDisplay
import kotlinx.android.synthetic.main.activity_hillfort.hillFortVisited
import kotlinx.android.synthetic.main.activity_hillfort.hillfortDescription
import kotlinx.android.synthetic.main.activity_hillfort.hillfortTitle
import kotlinx.android.synthetic.main.card_hillfort.*
import org.jetbrains.anko.intentFor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app : MainApp
    val IMAGE_REQUEST = 1
    var imageIndex = 0
    val LOCATION_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        var edit = false
        app = application as MainApp
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        info("Hillfort Activity started..")

        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            hillfortTitle.setText(hillfort.title)
            hillfortDescription.setText(hillfort.description)
            btnAdd.setText(R.string.save_hillfort)
            btnDeleteImage.visibility = View.VISIBLE
            if (hillfortLocation != null){
                hillFortLocationDisplay.visibility = View.VISIBLE
            }

            if (hillfort.visited) {
                hillFortVisited.isChecked = true
                hillFortDateVisited.setText(hillfort.dateVisited)
                hillFortDateVisited.visibility = View.VISIBLE
            } else
                hillFortDateVisited.visibility = View.GONE

            if (hillfort.image.size > 0) {
                chooseImage.setText(R.string.change_hillfort_image)
                hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image[imageIndex]))
            } else if (hillfort.image.size == 0){
                btnDeleteImage.visibility = View.GONE
            }

            var strLocation = "Latitude: " + hillfort.location.lat.toString() + "\nLongitude: " +hillfort.location.lng.toString() + "\nZoom: " +hillfort.location.zoom.toString()
            hillFortLocationDisplay.text = strLocation
        }

        toolbarAdd.setOnClickListener{
            app.users.deleteHillfort(app.currentUser, hillfort)
            finish()
        }

        btnAdd.setOnClickListener() {
            hillfort.title = hillfortTitle.text.toString()
            hillfort.description = hillfortDescription.text.toString()
            hillfort.dateVisited = hillFortDateVisited.text.toString()

            //var checkDate = false
            //try {
            //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //        LocalDate.parse(hillFortDateVisited.text.toString(), DateTimeFormatter.ISO_DATE)
            //       checkDate = true
            //    }
            //}catch (e: Exception) {
            //    info { e }
            //}
            //if (checkDate || !hillfort.visited){
            //   hillfort.dateVisited = hillFortDateVisited.text.toString()
            //} else
            //    toast("Please enter a valid date")

            if (hillfort.title.isEmpty()) {
                toast(R.string.enter_hillfort_title)
            } else {
                if (edit) {
                    app.users.updateHillfort(app.currentUser, hillfort.copy())
                } else {
                    app.users.createHillfort(app.currentUser, hillfort.copy())
                }
            }
            info("add Button Pressed: $hillfortTitle")
            info("$hillfort")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

        hillfortImage.setOnClickListener {
            if (hillfort.image.size != 0){
                imageIndex+=1
                if (imageIndex == hillfort.image.size){
                    imageIndex = 0
                }
                hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image[imageIndex]))
            }
        }

        btnDeleteImage.setOnClickListener() {
            hillfort.image.removeAt(imageIndex)
            imageIndex = 0
            if (hillfort.image.size == 0){
                hillfortImage.visibility = View.GONE
                btnDeleteImage.visibility = View.GONE
            }else
                hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image[imageIndex]))
        }

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        hillfortLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (hillfort.location.zoom != 0f) {
                location.lat =  hillfort.location.lat
                location.lng = hillfort.location.lng
                location.zoom = hillfort.location.zoom
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

        hillFortVisited.setOnClickListener{
            hillfort.visited = hillFortVisited.isChecked.toString().toBoolean()
            if (!hillFortVisited.isChecked){
                hillFortDateVisited.visibility = View.GONE
            }
            else
                hillFortDateVisited.visibility = View.VISIBLE
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
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
            R.id.item_cancel -> {
                finish()
            }
            R.id.hillfort_delete -> {
                app.users.deleteHillfort(app.currentUser, hillfort)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.image.add(data.getData().toString())
                    hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_hillfort_image)
                    btnDeleteImage.visibility = View.VISIBLE
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    hillfort.location.lat = location.lat
                    hillfort.location.lng = location.lng
                    hillfort.location.zoom = location.zoom
                    var strLocation = "Latitude: " + hillfort.location.lat.toString() + "\nLongitude: " +hillfort.location.lng.toString() + "\nZoom: " +hillfort.location.zoom.toString()
                    hillFortLocationDisplay.text = strLocation
                    hillFortLocationDisplay.visibility = View.VISIBLE
                }
            }
        }
    }
}