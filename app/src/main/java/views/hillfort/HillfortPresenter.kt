package views.hillfort

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hillfort.R
import com.example.hillfort.activities.MapsActivity
import com.example.hillfort.helpers.readImageFromPath
import com.example.hillfort.helpers.showImagePicker
import com.example.hillfort.main.MainApp
import com.example.hillfort.models.HillfortModel
import com.example.hillfort.models.Location
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HillfortPresenter(val view: HillfortView){
    var hillfort = HillfortModel()
    lateinit var app : MainApp
    val IMAGE_REQUEST = 1
    var imageIndex = 0
    val LOCATION_REQUEST = 2
    var edit = false
    var checkDate = false
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH )

    init {
        app = view.application as MainApp
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.hillfortTitle.setText(hillfort.title)
            view.hillfortDescription.setText(hillfort.description)
            view.btnAdd.setText(R.string.save_hillfort)
            view.btnDeleteImage.visibility = View.VISIBLE
            view.notes.setText(hillfort.notes)

            //checks if a location is set, if so, then the location view is made visible
            if (view.hillfortLocation != null){
                view.hillFortLocationDisplay.visibility = View.VISIBLE
            }

            //checks if hillfort has been visted
            if (hillfort.visited) {

                //if true the box is ticked and the date is set
                view.hillFortVisited.isChecked = true
                view.hillFortDateVisited.setText(hillfort.dateVisited)
                view.hillFortDateVisited.visibility = View.VISIBLE

                //if there was a date entered
                if (hillfort.dateVisited != ""){
                    var date = view.hillFortDateVisited.text.toString()

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
                        view.toast("Please enter a valid date")
                }
                //if visited is not checked, the date defaults to "" and the input field is invisible
            } else{
                view.hillFortDateVisited.visibility = View.GONE
                hillfort.dateVisited = ""
            }

            //if images were added to the hillfort
            if (hillfort.image.size > 0) {

                //image view is set to display the image
                view.hillfortImage.setImageBitmap(readImageFromPath(view, hillfort.image[imageIndex]))

                // if no images were added
            } else if (hillfort.image.size == 0){

                //image view defaults to logo
                view.hillfortImage.setImageDrawable(ContextCompat.getDrawable(view.applicationContext, R.drawable.ic_launcher_round))

                //and the delete button is hidden
                view.btnDeleteImage.visibility = View.GONE
            }

            //formatting the location for cleaner presentation
            var strLocation = "Latitude: " + hillfort.location.lat.toString() + "\nLongitude: " +hillfort.location.lng.toString() + "\nZoom: " +hillfort.location.zoom.toString()
            view.hillFortLocationDisplay.text = strLocation

            view.showHillfort(hillfort)
        }
    }

    fun doAddOrSave(title: String, description: String, notes: String, date: String) {
        hillfort.title = title
        hillfort.description = description
        hillfort.notes = notes
        hillfort.dateVisited = date
        if (edit) {
            //if user is editing a hillfort, then update
            app.users.updateHillfort(app.currentUser, hillfort.copy())

            //otherwise the user is creating a new hillfort
        } else {
            app.users.createHillfort(app.currentUser, hillfort.copy())
        }
        view.info("add Button Pressed: $ view.hillfortTitle")
        view.info("$hillfort")
        view.setResult(AppCompatActivity.RESULT_OK)
        view.finish()
    }

    fun doCancel(){
        view.finish()
    }

    fun doDelete(){
        app.users.deleteHillfort(app.currentUser, hillfort)
        view.finish()
    }

    fun doSelectImage(){
        showImagePicker(view, IMAGE_REQUEST)
    }

    fun doRemoveImage(){
        //remove the image at the currently displayed index
        hillfort.image.removeAt(imageIndex)

        //reset the index, displays the first image in the array
        imageIndex = 0

        //if there are not images
        if (hillfort.image.size == 0){

            //the delete button is gone, the button once again displays add image, and the default logo is displayed
            view.btnDeleteImage.visibility = View.GONE
            view.chooseImage.setText("Add Image")
            view.hillfortImage.setImageDrawable(ContextCompat.getDrawable(view.applicationContext, R.drawable.ic_launcher_round))
        }else

        //otherwise, the image at the image index is displayed
            view.hillfortImage.setImageBitmap(readImageFromPath(view, hillfort.image[imageIndex]))
    }

    fun doIterateImage(){
        //if there are images in a hillfort iterate the image index changing the image displayed
        if (hillfort.image.size != 0){
            imageIndex+=1

            //if the user gets to the end of the list, go back to the start of the list
            if (imageIndex == hillfort.image.size){
                imageIndex = 0
            }

            //displays the image at whichever image index, tapping on the image when there are more than 1 will go to the next image
            view.hillfortImage.setImageBitmap(readImageFromPath(view, hillfort.image[imageIndex]))
        }
    }
    fun doHillfortVisited(){
        //stores visited as true or false
        hillfort.visited = view.hillFortVisited.isChecked.toString().toBoolean()

        //if not checked
        if (!view.hillFortVisited.isChecked){
            //date text field is gone
            view.hillFortDateVisited.visibility = View.GONE
        }
        else
        //if checked its visible
            view.hillFortDateVisited.visibility = View.VISIBLE
    }

    fun doSetLocation(){
        //default location of WIT
        val location = Location(52.245696, -7.139102, 15f)
        if (hillfort.location.zoom != 0f) {
            location.lat =  hillfort.location.lat
            location.lng = hillfort.location.lng
            location.zoom = hillfort.location.zoom
        }
        //starts up the map activity
        view.startActivityForResult(view.intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
    }

    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.image.add(data.data.toString())
                    view.hillfortImage.setImageBitmap(readImageFromPath(view, hillfort.image[imageIndex]))
                    view.btnDeleteImage.visibility = View.VISIBLE
                    if(edit)
                        view.showHillfort(hillfort)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    hillfort.location.lat = location.lat
                    hillfort.location.lng = location.lng
                    hillfort.location.zoom = location.zoom
                    var strLocation = "Latitude: " + hillfort.location.lat.toString() + "\nLongitude: " +hillfort.location.lng.toString() + "\nZoom: " +hillfort.location.zoom.toString()
                    view.hillFortLocationDisplay.text = strLocation
                    view.hillFortLocationDisplay.visibility = View.VISIBLE
                    if(edit)
                        view.showHillfort(hillfort)
                }
            }
        }
    }
}