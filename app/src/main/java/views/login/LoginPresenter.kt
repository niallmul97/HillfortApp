package views.login

import com.google.firebase.auth.FirebaseAuth
import main.MainApp
import org.jetbrains.anko.toast
import views.Base.BasePresenter
import views.Base.BaseView
import views.Base.VIEW

class LoginPresenter(view: BaseView) : BasePresenter(view) {

    init{
        app = view.application as MainApp
    }

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                var XDLOOKOVERHERE = auth.currentUser?.email
                app.currentUser = app.users.findByEmail(auth.currentUser?.email)!!
                view?.navigateTo(VIEW.LIST)
            } else {
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
            view?.hideProgress()
        }
    }

    fun doRegister(email: String, password: String) {
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                app.currentUser = app.users.findByEmail(auth.currentUser?.email)!!
                view?.navigateTo(VIEW.LIST)
            } else {
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
            view?.hideProgress()
        }
    }
}