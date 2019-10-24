package com.example.hillfort

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        info("Hillfort Activity started..")

        btnAdd.setOnClickListener() {
            val hillfortTitle = hillfortTitle.text.toString()
            if (hillfortTitle.isNotEmpty()) {
                info("add Button Pressed: $hillfortTitle")
            }
            else {
                toast ("Please Enter a title")
            }
        }
    }
}