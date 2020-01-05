package views.hillfortList

import adapters.HillfortAdapter
import adapters.HillfortListener
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hillfort.R
import views.login.LoginView
import views.settings.SettingsView
import main.MainApp
import com.example.hillfort.models.HillfortModel
import com.example.hillfort.models.UserModel
import com.google.android.material.bottomnavigation.BottomNavigationPresenter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.content_bottom_nav.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import views.Base.BaseView
import views.Base.VIEW
import views.BottomNavPresenter

class HillfortListView : BaseView(), HillfortListener {

    lateinit var presenter: HillfortListPresenter
    lateinit var bottomNav: BottomNavPresenter
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter
        bottomNav = initPresenter(BottomNavPresenter(this)) as BottomNavPresenter

        app = MainApp()
        //creates toolbar
        super.init(toolbar, false)

        item_home.setOnClickListener(){
            bottomNav.toHome()
        }

        item_map.setOnClickListener(){
            bottomNav.toMaps()
        }

        item_settings.setOnClickListener(){
            bottomNav.toSettings()
        }

        //recycle viewer is used to display all hillforts
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.getHillforts()
    }

    override fun showHillforts(hillforts: List<HillfortModel>){
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
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
                FirebaseAuth.getInstance().signOut()
                navigateTo(VIEW.LOGIN)
                toast("Logout Successful")
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