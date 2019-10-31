package com.example.hillfort.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.hillfort.R
import com.example.hillfort.main.MainApp
import com.example.hillfort.models.UserModel
import kotlinx.android.synthetic.main.activity_hillfort_list.*

import kotlinx.android.synthetic.main.activity_login.*
import models.generateRandomId
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.email
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = application as MainApp
        buttonLogin.setOnClickListener{
            if (editEmail != null && editPassword != null){
                var result = app.users.login(editEmail.text.toString(), editPassword.text.toString())
                if (result){
                    var currentUser = app.users.findByEmail(editEmail.text.toString())
                    if (currentUser != null){
                        app.currentUser = currentUser
                        startActivity(intentFor<HillfortList>())
                    }
                }else (toast("Please enter a correct Email and Password"))
            }
        }
        buttonRegister.setOnClickListener{
            if (editEmail != null && editPassword != null){
                val result = app.users.findByEmail(editEmail.text.toString())
                if (result == null){
                    app.users.create(UserModel(generateRandomId(), editEmail.text.toString(), editPassword.text.toString()))
                    toast("user created")
                    val resultLogin = app.users.login(editEmail.text.toString(), editPassword.text.toString())
                    if (resultLogin){
                        var currentUser = app.users.findByEmail(editEmail.text.toString())
                        if (currentUser != null){
                            app.currentUser = currentUser
                            startActivity(intentFor<HillfortList>())
                        }
                    }else (toast("A user with that email already exists"))
                }
            }
        }
    }
}
