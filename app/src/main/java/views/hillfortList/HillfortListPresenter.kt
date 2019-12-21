package views.hillfortList

import adapters.HillfortAdapter
import com.example.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import views.Base.BasePresenter
import views.Base.BaseView
import views.Base.VIEW

class HillfortListPresenter(view: BaseView) : BasePresenter(view) {

    fun getHillforts(){
        view?.showHillforts(app.users.findAllHillforts(app.currentUser))
    }

    fun doAddHillfort(){
        view?.navigateTo(VIEW.HILLFORT)
    }

    fun doEditHillfort(hillfort: HillfortModel){
        view?.navigateTo(VIEW.HILLFORT, 0, "hillfort_edit", hillfort)
    }

    fun doShowHillfortsMap(){
        view?.navigateTo(VIEW.MAPS)
    }
}