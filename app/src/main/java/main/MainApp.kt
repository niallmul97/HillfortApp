package com.example.hillfort.main

import android.app.Application
import com.example.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.activity_hillfort.*
import models.HillfortMemStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    //val hillforts = ArrayList<HillfortModel>()
    val hillforts = HillfortMemStore()

    override fun onCreate() {
        super.onCreate()
        info("Hillfort started")
    }
}