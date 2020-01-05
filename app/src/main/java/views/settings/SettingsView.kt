package views.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hillfort.R
import main.MainApp
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_bottom_nav.*
import org.jetbrains.anko.AnkoLogger
import views.Base.BaseView
import views.BottomNavPresenter

class SettingsView : BaseView(), AnkoLogger {

    lateinit var app : MainApp
    lateinit var presenter: SettingsPresenter
    lateinit var bottomNav: BottomNavPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        presenter = SettingsPresenter(this)
        bottomNav = initPresenter(BottomNavPresenter(this)) as BottomNavPresenter
        setContentView(R.layout.activity_settings)

        //toolbar is setup
        toolbarSettings.title = title
        setSupportActionBar(toolbarSettings)

        //up button activated
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        item_home.setOnClickListener(){
            bottomNav.toHome()
        }

        item_map.setOnClickListener(){
            bottomNav.toMaps()
        }

        item_settings.setOnClickListener(){
            bottomNav.toSettings()
        }



        //displays the users email
        presenter.doShowEmail()

        //displays the users password (not as plain text, current password cannot be seen)
        presenter.doShowPassword()

        //displays the current amount of hillforts the logged in user has added
        presenter.doTotalHillforts()

        //displays total visits
        presenter.doTotalVisits()

        //displays total images
        presenter.doTotalImages()

        //displays oldest and most recent visits
        presenter.doRecentAndOldestVisits()

        //when the update email button is pressed
        updateEmail.setOnClickListener {presenter.doUpdateEmail()}

        //when the update password button is pressed
        updatePassword.setOnClickListener {presenter.doUpdatePassword()}
    }

    //up button is setup to act like the back button
    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}
