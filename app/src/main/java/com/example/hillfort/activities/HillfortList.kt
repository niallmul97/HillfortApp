package com.example.hillfort.activities

import adapters.HillfortAdapter
import adapters.HillfortListener
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hillfort.R
import com.example.hillfort.main.MainApp
import com.example.hillfort.models.HillfortModel
import com.example.hillfort.models.UserModel
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class HillfortList : AppCompatActivity(), HillfortListener {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        app = application as MainApp

        //creates toolbar
        toolbar.title = title
        setSupportActionBar(toolbar)

        //recycle viewer is used to display all hillforts
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        loadHillforts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            //when the add hillfort button is pressed, the hillfort activity is started
            R.id.item_add -> startActivityForResult<HillfortActivity>(0)

            //when the map button is pressed, the hillfort map activity is started
            R.id.item_map -> startActivity<HillfortMapsActivity>()

            //when the logout option is pressed, the current user is no longer the current user and the login activity is started
            R.id.logout ->{
                app.currentUser = UserModel()
                toast("Logout Successful")
                startActivityForResult<LoginActivity>(0)
                finish()
            }

            //when the settings option is pressed, the settings activity is started
            R.id.settings ->{
                startActivity<SettingsActivity>()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //when a hillfort is clicked, hillfort activity for that hillfort is started, the user can now edit the hillfort
    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort), 0)
    }

    //calls the find all hillforts function to display them in the recycle viewer
    private fun loadHillforts() {
        showHillforts(app.users.findAllHillforts(app.currentUser))
    }

    fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        recyclerView.adapter?.notifyDataSetChanged()
        loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }
}