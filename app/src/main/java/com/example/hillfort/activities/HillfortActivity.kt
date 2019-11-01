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
import androidx.core.content.ContextCompat
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
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader
import kotlinx.android.synthetic.main.activity_hillfort.hillFortLocationDisplay
import kotlinx.android.synthetic.main.activity_hillfort.hillFortVisited
import kotlinx.android.synthetic.main.activity_hillfort.hillfortDescription
import kotlinx.android.synthetic.main.activity_hillfort.hillfortTitle
import kotlinx.android.synthetic.main.card_hillfort.*
import org.jetbrains.anko.intentFor
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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
        var checkDate = false
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH )

        app = application as MainApp

        //Sets up toolbar
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        info("Hillfort Activity started..")

        //Checks if we are editing an existing hillfort
        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            hillfortTitle.setText(hillfort.title)
            hillfortDescription.setText(hillfort.description)
            btnAdd.setText(R.string.save_hillfort)
            btnDeleteImage.visibility = View.VISIBLE
            notes.setText(hillfort.notes)

            //checks if a location is set, if so, then the location view is made visible
            if (hillfortLocation != null){
                hillFortLocationDisplay.visibility = View.VISIBLE
            }

            //checks if hillfort has been visted
            if (hillfort.visited) {

                //if true the box is ticked and the date is set
                hillFortVisited.isChecked = true
                hillFortDateVisited.setText(hillfort.dateVisited)
                hillFortDateVisited.visibility = View.VISIBLE

                //if there was a date entered
                if (hillfort.dateVisited != ""){
                    var date = hillFortDateVisited.text.toString()

                    //the "date" is validated as a date
                    try {
                        formatter.parse(date)
                        checkDate = true

                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                    //if the "date" is a valid date, then it is set as the date visited
                    if (checkDate){
                        hillfort.dateVisited = date
                    } else
                        toast("Please enter a valid date")
                }
                //if visited is not checked, the date defaults to "" and the input field is invisible
            } else{
                hillFortDateVisited.visibility = View.GONE
                hillfort.dateVisited = ""
            }

            //if images were added to the hillfort
            if (hillfort.image.size > 0) {

                //button changes to add another image as opposed to add image
                chooseImage.setText(R.string.change_hillfort_image)

                //image view is set to display the image
                hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image[imageIndex]))

                // if no images were added
            } else if (hillfort.image.size == 0){

                //image view defaults to logo
                hillfortImage.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_launcher_round))

                //and the delete button is hidden
                btnDeleteImage.visibility = View.GONE
            }

            //formatting the location for cleaner presentation
            var strLocation = "Latitude: " + hillfort.location.lat.toString() + "\nLongitude: " +hillfort.location.lng.toString() + "\nZoom: " +hillfort.location.zoom.toString()
            hillFortLocationDisplay.text = strLocation
        }

        //button for the delete icon in the toolbar, deletes current hillfort (see onPrepareOptionsMenu() below)
        toolbarAdd.setOnClickListener{
            app.users.deleteHillfort(app.currentUser, hillfort)
            finish()
        }

        //Adds the hillfort
        btnAdd.setOnClickListener() {
            hillfort.title = hillfortTitle.text.toString()
            hillfort.description = hillfortDescription.text.toString()
            hillfort.notes = notes.text.toString()

            //checks if hillfort has been visted
            if (hillFortVisited.isChecked){
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
                } else

                    //otherwise add a valid date
                    toast("Please enter a valid date")
            } else{
                //if hillfort isn't visited, date defaults to "" and textfield is gone
                hillFortDateVisited.visibility = View.GONE
                hillfort.dateVisited = ""
            }

            //makes sure title is not empty
            if (hillfort.title.isEmpty()) {
                toast(R.string.enter_hillfort_title)
            } else {
                if (edit) {
                    //if user is editing a hillfort, then update
                    app.users.updateHillfort(app.currentUser, hillfort.copy())

                    //otherwise the user is creating a new hillfort
                } else {
                    app.users.createHillfort(app.currentUser, hillfort.copy())
                }
            }
            info("add Button Pressed: $hillfortTitle")
            info("$hillfort")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

        //when a user clicks the image
        hillfortImage.setOnClickListener {

            //if there are images in a hillfort iterate the image index changing the image displayed
            if (hillfort.image.size != 0){
                imageIndex+=1

                //if the user gets to the end of the list, go back to the start of the list
                if (imageIndex == hillfort.image.size){
                    imageIndex = 0
                }

                //displays the image at whichever image index, tapping on the image when there are more than 1 will go to the next image
                hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image[imageIndex]))
            }
        }

        //when the delete image button is pressed
        btnDeleteImage.setOnClickListener() {

            //remove the image at the currently displayed index
            hillfort.image.removeAt(imageIndex)

            //reset the index, displays the first image in the array
            imageIndex = 0

            //if there are not images
            if (hillfort.image.size == 0){

                //the delete button is gone, the button once again displays add image, and the default logo is displayed
                btnDeleteImage.visibility = View.GONE
                chooseImage.setText("Add Image")
                hillfortImage.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_launcher_round))
            }else

                //otherwise, the image at the image index is displayed
                hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image[imageIndex]))
        }

        //when the add image button is clicked, opens the image picker
        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        //when the set location button is pressed
        hillfortLocation.setOnClickListener {

            //default location of WIT
            val location = Location(52.245696, -7.139102, 15f)
            if (hillfort.location.zoom != 0f) {
                location.lat =  hillfort.location.lat
                location.lng = hillfort.location.lng
                location.zoom = hillfort.location.zoom
            }
            //starts up the map activity
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

        //if the visited box is checked
        hillFortVisited.setOnClickListener{

            //stores visited as true or false
            hillfort.visited = hillFortVisited.isChecked.toString().toBoolean()

            //if not checked
            if (!hillFortVisited.isChecked){
                //date text field is gone
                hillFortDateVisited.visibility = View.GONE
            }
            else
                //if checked its visible
                hillFortDateVisited.visibility = View.VISIBLE
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
                finish()
            }

            //when the delete button is pressed, the hillfort is deleted and the user is brought back and activity
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
                    hillfort.image.add(data.data.toString())
                    hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image[imageIndex]))
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