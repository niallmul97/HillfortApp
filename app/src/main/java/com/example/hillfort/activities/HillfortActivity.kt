package com.example.hillfort.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.hillfort.R
import com.example.hillfort.helpers.readImage
import com.example.hillfort.helpers.readImageFromPath
import com.example.hillfort.helpers.showImagePicker
import com.example.hillfort.main.MainApp
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import com.example.hillfort.models.HillfortModel

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app : MainApp
    val IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        var edit = false
        app = application as MainApp
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        info("Hillfort Activity started..")

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            hillfortTitle.setText(hillfort.title)
            hillfortDescription.setText(hillfort.description)
            btnAdd.setText(R.string.save_hillfort)
            hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
            if (hillfort.image != null) {
                chooseImage.setText(R.string.change_hillfort_image)
            }
            hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
        }

        btnAdd.setOnClickListener() {
            hillfort.title = hillfortTitle.text.toString()
            hillfort.description = hillfortDescription.text.toString()
            if (hillfort.title.isEmpty()) {
                toast(R.string.enter_hillfort_title)
            } else {
                if (edit) {
                    app.hillforts.update(hillfort.copy())
                } else {
                    app.hillforts.create(hillfort.copy())
                }
            }
            info("add Button Pressed: $hillfortTitle")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.image = data.getData().toString()
                    hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_hillfort_image)
                }
            }
        }
    }
}