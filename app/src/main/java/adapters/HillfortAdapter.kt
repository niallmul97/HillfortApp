package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hillfort.R
import com.example.hillfort.helpers.readImageFromPath
import com.example.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort.view.*
import kotlinx.android.synthetic.main.card_hillfort.view.*
import kotlinx.android.synthetic.main.card_hillfort.view.hillFortLocationDisplay
import kotlinx.android.synthetic.main.card_hillfort.view.hillFortVisited
import kotlinx.android.synthetic.main.card_hillfort.view.hillfortDescription
import kotlinx.android.synthetic.main.card_hillfort.view.hillfortTitle

interface HillfortListener {
    fun onHillfortClick(hillfort: HillfortModel)
}
class HillfortAdapter constructor(
    private var hillforts: List<HillfortModel>,
    private val listener: HillfortListener
) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_hillfort,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hillfort: HillfortModel, listener: HillfortListener) {
            itemView.imageIcon.visibility = View.VISIBLE
            itemView.hillfortTitle.text = hillfort.title
            itemView.hillfortDescription.text = hillfort.description
            var strLocation = "Latitude: " + hillfort.location.lat.toString() + "\nLongitude: " +hillfort.location.lng.toString() + "\nZoom: " +hillfort.location.zoom.toString()
            itemView.hillFortLocationDisplay.text = strLocation
            itemView.hillFortVisited.isChecked = hillfort.visited
            if (hillfort.image.size > 0){
                itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, hillfort.image[0]))
            }
            else if (hillfort.image.size == 0){
                itemView.imageIcon.visibility = View.INVISIBLE
            }
            itemView.setOnClickListener{listener.onHillfortClick(hillfort)}
        }
    }
}