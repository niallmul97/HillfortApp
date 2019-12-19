package com.example.hillfort.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hillfort.R
import com.example.hillfort.main.MainApp
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SettingsActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        setContentView(R.layout.activity_settings)

        //toolbar is setup
        toolbarSettings.title = title
        setSupportActionBar(toolbarSettings)

        //up button activated
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //displays the users email
        displayEmail.setText(app.currentUser.email)

        //displays the users password (not as plain text, current password cannot be seen)
        displayPassword.setText(app.currentUser.password)

        //displays the current amount of hillforts the logged in user has added
        displayTotalHillforts.setText("Total Hillforts: "+ app.currentUser.hillforts.size)

        //count of visited hillforts
        var visitedCount = 0

        //date formatter
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH )

        //loops through all the current users' hillforts and iterates the count for each one visited
        for (hillforts in app.currentUser.hillforts){
            if (hillforts.visited){
                visitedCount++
            }
        }

        //display the number of visited hillforts
        displayTotalHillfortsVisited.setText("Total Hillforts Visited: " +visitedCount)

        //count for images
        var imageCount = 0

        //image count is set to the total number of images the current user has uploaded
        app.currentUser.hillforts.forEach{
            imageCount += it.image.size
        }

        //displays the amount of images
        displayTotalImages.setText("Total Images: "+imageCount)

        //initializes and arraylist for all the dates
        var allDates: ArrayList<Date?> = ArrayList()

        //loops through all the dates visited for every hillfort
        app.currentUser.hillforts.forEach{

            //when the date visited is not "", the date parsed as a valid date and is added to the arraylist
            if (it.dateVisited != ""){
                allDates.add(formatter.parse(it.dateVisited))
            }
        }

        //so long as there is something in the arraylist
        if (allDates.size > 0){

            //the oldest visit becomes the first date in the arraylist
            var oldestVisited = allDates[0]

            //the newest visit becomes the first date in the arraylist
            var newestVisited = allDates[0]

            //loops through the arraylist
            for (i in allDates){

                //if any of the dates in the list are before the current oldest, that older date is now the new oldest date
                if (i!!.before(oldestVisited)){
                    oldestVisited = i
                }

                //if any of the dates in the list are after the current newest, that newer date is now the new newest date
                if (i.after(newestVisited)){
                    newestVisited = i
                }
            }
            //displays the newest date
            displayMostRecentlyVisited.setText("Most Recent Hillfort Visit: "+newestVisited)

            //displays the oldest date
            displayOldestVisited.setText("Oldest Hillfort Visit: "+oldestVisited)

        }else{
            //otherwise display that the newest and oldest dates are N/A
            displayMostRecentlyVisited.setText("Most Recent Hillfort Visit: N/A")
            displayOldestVisited.setText("Oldest Hillfort Visit: N/A")
        }

        //when the update email button is pressed
        updateEmail.setOnClickListener {

            //new email string is initialized
            var newEmail = ""

            //so long as the email in the text field is not the same as the current user's current email
            if (displayEmail.text.toString() != app.currentUser.email){

                //that email in the the text field is the aforementioned "newEmail"
                newEmail = displayEmail.text.toString()

                //if the newEmail is not ""
                if (newEmail != ""){

                    //if a user with the "newEmail" cannot be found
                    if (app.users.findByEmail(newEmail) == null){

                        //then current user's email is updated to the newEmail
                        app.currentUser.email = newEmail
                        app.users.update(app.currentUser)
                        toast("Email updated")

                        //otherwise a user with that email already exists
                    } else toast("A user with that email already exists")

                    //the email entered was ""
                }else toast("Please enter a valid email")

                //the email entered was the same as the current one
            }else toast("Please enter a different email to your current one")
        }

        //when the update password button is pressed
        updatePassword.setOnClickListener {

            //newPassword is initialized as ""
            var newPassword = ""

            //if the password in the text field is not the same as the current user's current password
            if (displayPassword.text.toString() != app.currentUser.password){

                //then the newPassword is equal to what is in the text field
                newPassword = displayPassword.text.toString()

                //so log as the newPassword isn't "", the newPassword is set and the user is updated
                if (newPassword != ""){
                    app.currentUser.password = newPassword
                    app.users.update(app.currentUser)
                    toast("Password Updated")
                }else toast("Please enter a valid password")
            }else toast("Please enter a different password to your current one")
        }
    }

    //up button is setup to act like the back button
    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}
