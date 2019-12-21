package views.login

import com.example.hillfort.main.MainApp
import com.example.hillfort.models.UserModel
import kotlinx.android.synthetic.main.activity_login.*
import models.generateRandomId
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import views.hillfortList.HillfortListView

class LoginPresenter(val view: LoginView) {
    var app: MainApp

    init {
        app = view.application as MainApp
    }

    fun doLogin(){
        //if both the email and password inputs are not null
        if (view.editEmail != null && view.editPassword != null){

            //boolean that attempts to sign in with the email and password entered
            var result = app.users.login(view.editEmail.text.toString(), view.editPassword.text.toString())

            //if the login is a success
            if (result){

                //boolean to try and find the user with the email entered
                var currentUser = app.users.findByEmail(view.editEmail.text.toString())

                //if the return is true, then that user is the current user and said user's hillfort list is displayed
                if (currentUser != null){
                    app.currentUser = currentUser
                    view.startActivity<HillfortListView>()
                }

                //if the login fails, user is prompted to enter valid data
            }else (view.toast("Please enter a correct Email and Password"))
        }
    }

    fun doRegister(){
        //checks that the email and password are not null
        if (view.editEmail != null && view.editPassword != null){

            //tries find the user via the email provided
            val result = app.users.findByEmail(view.editEmail.text.toString())

            //if no user with that email exists
            if (result == null){

                //then a new user with the data provided is created
                app.users.create(UserModel(generateRandomId(), view.editEmail.text.toString(), view.editPassword.text.toString()))
                view.toast("user created")

                //user is then logged in and their hillfort list activity is started
                val resultLogin = app.users.login(view.editEmail.text.toString(), view.editPassword.text.toString())
                if (resultLogin){
                    var currentUser = app.users.findByEmail(view.editEmail.text.toString())
                    if (currentUser != null){
                        app.currentUser = currentUser
                        view.startActivity<HillfortListView>()
                    }

                    //if user with the provided email exists then the user is prompted as such
                }else (view.toast("A user with that email already exists"))
            }
        }
    }
}