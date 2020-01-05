package views.login

import android.os.Bundle
import android.view.View
import com.example.hillfort.R
import com.example.hillfort.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import main.MainApp
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import views.Base.BaseView

class LoginView : BaseView() {

    lateinit var presenter: LoginPresenter
    lateinit var app: MainApp
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBar.visibility = View.GONE
        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter
        app = MainApp()
        auth = FirebaseAuth.getInstance()

        //when the login button is pressed
        buttonLogin.setOnClickListener{
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            }
            else {
                presenter.doLogin(email,password)
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
                presenter.doRegister(email,password)
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
