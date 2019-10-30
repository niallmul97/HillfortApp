/**package models

import android.content.Context
import com.example.hillfort.models.HillfortModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import helpers.read
import helpers.write
import org.jetbrains.anko.AnkoLogger
import com.example.hillfort.helpers.*
import helpers.exists
import org.jetbrains.anko.info
import java.util.*

val JSON_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HillfortJSONStore : HillfortStore, AnkoLogger {

    val context: Context
    var hillforts = mutableListOf<HillfortModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserializeHillfort()
        }
    }

    override fun findAllHillforts(): MutableList<HillfortModel> {
        return hillforts
    }

    override fun createHillfort(hillfort: HillfortModel) {
        hillfort.id = generateRandomId()
        hillforts.add(hillfort)
        serializeHillfort()
    }

    override fun deleteHillfort(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
        serializeHillfort()
    }

    override fun updateHillfort(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.image = hillfort.image
            foundHillfort.visited = hillfort.visited
            foundHillfort.dateVisited = hillfort.dateVisited
            foundHillfort.notes = hillfort.notes
            foundHillfort.location.lat = hillfort.location.lat
            foundHillfort.location.lng = hillfort.location.lng
            foundHillfort.location.zoom = hillfort.location.zoom
            logAllHillforts()
        }
    }

    fun logAllHillforts() {
        hillforts.forEach{ info("${it}") }
    }

    private fun serializeHillfort() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserializeHillfort() {
        val jsonString = read(context, JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }
}**/