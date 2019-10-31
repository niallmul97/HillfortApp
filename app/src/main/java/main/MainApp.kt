package com.example.hillfort.main

import android.app.Application
import com.example.hillfort.models.HillfortModel
import com.example.hillfort.models.UserModel
import kotlinx.android.synthetic.main.activity_hillfort.*
import models.UserJSONStore
import models.HillfortMemStore
import models.HillfortStore
import models.UserStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    //val hillforts = ArrayList<HillfortModel>()
    //val hillforts = HillfortMemStore()

    lateinit var users: UserStore
    lateinit var currentUser: UserModel

    override fun onCreate() {
        super.onCreate()
        users = UserJSONStore(applicationContext)
        info("Hillfort App started")
    }
}