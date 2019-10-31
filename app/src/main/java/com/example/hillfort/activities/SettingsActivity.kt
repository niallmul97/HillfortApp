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
        toolbarSettings.title = title
        setSupportActionBar(toolbarSettings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        displayEmail.setText(app.currentUser.email)
        displayPassword.setText(app.currentUser.password)
        displayTotalHillforts.setText("Total Hillforts: "+ app.currentUser.hillforts.size)
        var visitedCount = 0
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH )
        for (hillforts in app.currentUser.hillforts){
            if (hillforts.visited){
                visitedCount++
            }
        }
        displayTotalHillfortsVisited.setText("Total Hillforts Visited: " +visitedCount)

        var imageCount = 0
        app.currentUser.hillforts.forEach{
            imageCount += it.image.size
        }
        displayTotalImages.setText("Total Images: "+imageCount)


        var allDates: ArrayList<Date?> = ArrayList()
        app.currentUser.hillforts.forEach{
            allDates.add(formatter.parse(it.dateVisited))
        }
        var oldestVisited = allDates[0]
        var newestVisited = allDates[0]

        for (i in allDates){
            if (i!!.before(oldestVisited)){
                oldestVisited = i
            }
            if (i.after(newestVisited)){
                newestVisited = i
            }
        }
        displayMostRecentlyVisited.setText("Most Recent Hillfort Visit: "+newestVisited)
        displayOldestVisited.setText("Oldest Hillfort Visit: "+oldestVisited)

        updateEmail.setOnClickListener {
            var newEmail = ""
            //app.users.updateHillfort(app.currentUser, hillfort.copy())
            if (displayEmail.text.toString() != app.currentUser.email){
                newEmail = displayEmail.text.toString()
                if (newEmail != ""){
                    if (app.users.findByEmail(newEmail) == null){
                        app.currentUser.email = newEmail
                        app.users.update(app.currentUser)
                        toast("Email updated")
                    } else toast("A user with that email already exists")
                }else toast("Please enter a valid email")
            }else toast("Please enter a different email to your current one")
        }

        updatePassword.setOnClickListener {
            var newPassword = ""
            if (displayPassword.text.toString() != app.currentUser.password){
                newPassword = displayPassword.text.toString()
                if (newPassword != ""){
                    app.currentUser.password = newPassword
                    app.users.update(app.currentUser)
                    toast("Password Updated")
                }else toast("Please enter a valid password")
            }else toast("Please enter a different password to your current one")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}
