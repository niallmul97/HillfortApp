package views.hillfortList

import adapters.HillfortListener
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hillfort.R
import views.login.LoginView
import views.settings.SettingsView
import com.example.hillfort.main.MainApp
import com.example.hillfort.models.HillfortModel
import com.example.hillfort.models.UserModel
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class HillfortListView : AppCompatActivity(), HillfortListener {

    lateinit var presenter: HillfortListPresenter
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        presenter = HillfortListPresenter(this)
        app = MainApp()

        //creates toolbar
        toolbar.title = title
        setSupportActionBar(toolbar)

        //recycle viewer is used to display all hillforts
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.getHillforts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            //when the add hillfort button is pressed, the hillfort activity is started
            R.id.item_add -> presenter.doAddHillfort()

            //when the map button is pressed, the hillfort map activity is started
            R.id.item_map -> presenter.doShowHillfortsMap()

            //when the logout option is pressed, the current user is no longer the current user and the login activity is started
            R.id.logout ->{
                app.currentUser = UserModel()
                toast("Logout Successful")
                startActivityForResult<LoginView>(0)
                finish()
            }

            //when the settings option is pressed, the settings activity is started
            R.id.settings ->{
                startActivity<SettingsView>()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //when a hillfort is clicked, hillfort activity for that hillfort is started, the user can now edit the hillfort
    override fun onHillfortClick(hillfort: HillfortModel) {
        presenter.doEditHillfort(hillfort)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}