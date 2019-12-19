package com.example.hillfort.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hillfort.R
import com.example.hillfort.main.MainApp
import com.example.hillfort.models.UserModel

import kotlinx.android.synthetic.main.activity_login.*
import models.generateRandomId
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import views.hillfortList.HillfortList

class LoginActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = application as MainApp

        //when the login button is pressed
        buttonLogin.setOnClickListener{

            //if both the email and password inputs are not null
            if (editEmail != null && editPassword != null){

                //boolean that attempts to sign in with the email and password entered
                var result = app.users.login(editEmail.text.toString(), editPassword.text.toString())

                //if the login is a success
                if (result){

                    //boolean to try and find the user with the email entered
                    var currentUser = app.users.findByEmail(editEmail.text.toString())

                    //if the return is true, then that user is the current user and said user's hillfort list is displayed
                    if (currentUser != null){
                        app.currentUser = currentUser
                        startActivity(intentFor<HillfortList>())
                    }

                    //if the login fails, user is prompted to enter valid data
                }else (toast("Please enter a correct Email and Password"))
            }
        }

        //when the register button is pressed
        buttonRegister.setOnClickListener{

            //checks that the email and password are not null
            if (editEmail != null && editPassword != null){

                //tries find the user via the email provided
                val result = app.users.findByEmail(editEmail.text.toString())

                //if no user with that email exists
                if (result == null){

                    //then a new user with the data provided is created
                    app.users.create(UserModel(generateRandomId(), editEmail.text.toString(), editPassword.text.toString()))
                    toast("user created")

                    //user is then logged in and their hillfort list activity is started
                    val resultLogin = app.users.login(editEmail.text.toString(), editPassword.text.toString())
                    if (resultLogin){
                        var currentUser = app.users.findByEmail(editEmail.text.toString())
                        if (currentUser != null){
                            app.currentUser = currentUser
                            startActivity(intentFor<HillfortList>())
                        }

                        //if user with the provided email exists then the user is prompted as such
                    }else (toast("A user with that email already exists"))
                }
            }
        }
    }
}
