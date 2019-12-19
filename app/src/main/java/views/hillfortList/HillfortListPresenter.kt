package views.hillfortList

import adapters.HillfortAdapter
import views.editLocation.HillfortMapsActivity
import com.example.hillfort.main.MainApp
import com.example.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import views.hillfort.HillfortView

class HillfortListPresenter(val view: HillfortListView) {
    var app: MainApp

    init {
        app = view.application as MainApp
    }

    fun getHillforts(){
        doShowHillforts(app.users.findAllHillforts(app.currentUser))
    }

    fun doShowHillforts(hillforts: List<HillfortModel>){
        view.recyclerView.adapter = HillfortAdapter(hillforts, view)
        view.recyclerView.adapter?.notifyDataSetChanged()
    }

    fun doAddHillfort(){
        view.startActivityForResult<HillfortView>(0)
    }

    fun doEditHillfort(hillfort: HillfortModel){
        view.startActivityForResult(view.intentFor<HillfortView>().putExtra("hillfort_edit", hillfort), 0)
    }

    fun doShowHillfortsMap(){
        view.startActivity<HillfortMapsActivity>()
    }
}