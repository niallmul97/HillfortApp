package com.example.hillfort.main

import android.app.Application
import com.example.hillfort.models.UserModel
import models.UserJSONStore
import models.UserStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    lateinit var users: UserStore
    lateinit var currentUser: UserModel

    override fun onCreate() {
        super.onCreate()
        users = UserJSONStore(applicationContext)
        info("Hillfort App started")
    }
}