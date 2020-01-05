package views

import views.Base.BasePresenter
import views.Base.BaseView
import views.Base.VIEW

class BottomNavPresenter(view: BaseView): BasePresenter(view) {

    fun toMaps() {
        view?.navigateTo(VIEW.MAPS)

    }

    fun toHome() {
        view?.navigateTo(VIEW.LIST)

    }

    fun toSettings(){
        view?.navigateTo(VIEW.SETTINGS)
    }
}