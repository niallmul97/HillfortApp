package views.login

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
import views.hillfortList.HillfortListView

class LoginView : AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = application as MainApp
        presenter = LoginPresenter(this)

        //when the login button is pressed
        buttonLogin.setOnClickListener{
            presenter.doLogin()
        }

        //when the register button is pressed
        buttonRegister.setOnClickListener{
            presenter.doRegister()
        }
    }
}
