package com.example.hillfort.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hillfort.R
import com.example.hillfort.main.MainApp
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
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
        displayEmail.setText("Email: "+app.currentUser.email)
        displayPassword.setText("Password:"+app.currentUser.password)
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

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}
