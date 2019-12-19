package views.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hillfort.R
import com.example.hillfort.main.MainApp
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SettingsActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp
    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        presenter = SettingsPresenter(this)
        setContentView(R.layout.activity_settings)

        //toolbar is setup
        toolbarSettings.title = title
        setSupportActionBar(toolbarSettings)

        //up button activated
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
