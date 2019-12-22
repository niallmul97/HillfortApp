package views.login

import android.os.Bundle
import android.view.View
import com.example.hillfort.R
import main.MainApp

import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import views.Base.BaseView

class LoginView : BaseView() {

    lateinit var presenter: LoginPresenter
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBar.visibility = View.GONE
        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter
        app = MainApp()

        //when the login button is pressed
        buttonLogin.setOnClickListener{
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            }
            else {
                //boolean to try and find the user with the email entered
                val currentUser = app.users.findByEmail(email)

                //if the return is true, then that user is the current user and said user's hillfort list is displayed
                if (currentUser != null){
                    app.currentUser = currentUser
                    presenter.doLogin(email,password)
                }
            }
        }

        //when the register button is pressed
        buttonRegister.setOnClickListener{
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            }
            else {
                //boolean to try and find the user with the email entered
                var currentUser = app.users.findByEmail(email)

                //if the return is true, then that user is the current user and said user's hillfort list is displayed
                if (currentUser != null){
                    app.currentUser = currentUser
                    presenter.doRegister(email,password)
                }
            }
        }
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}
