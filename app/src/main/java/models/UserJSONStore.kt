package models

import android.content.Context
import com.example.hillfort.models.UserModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import helpers.read
import helpers.write
import org.jetbrains.anko.AnkoLogger
import com.example.hillfort.helpers.*
import com.example.hillfort.models.HillfortModel
import helpers.exists
import org.jetbrains.anko.info
import java.util.*

val JSON_FILE = "users.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<UserModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class UserJSONStore : UserStore, AnkoLogger {

    val context: Context
    var users = mutableListOf<UserModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun login(email: String, password: String): Boolean{
        val user = findByEmail(email)

        if (user != null){
            if (user.password == password){
                return true
            }
        }
        return false
    }

    override fun findAll(): MutableList<UserModel> {
        return users
    }

    override fun findByEmail(email: String): UserModel? {
        return users.find { user -> user.email == email }
    }

    override fun create(user: UserModel) {
        user.id = generateRandomId()
        users.add(user)
        serialize()
    }

    override fun delete(user: UserModel) {
        users.remove(user)
        serialize()
    }

    override fun update(user: UserModel) {
        val foundUser: UserModel? = users.find { p -> p.id == user.id }
        if (foundUser != null) {
            foundUser.email = user.email
            foundUser.password = user.password
            foundUser.id = user.id
            logAll()
            serialize()
        }
    }

    fun logAll() {
        users.forEach{ info("${it}") }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        users = Gson().fromJson(jsonString, listType)
    }

    override fun findAllHillforts(user: UserModel): MutableList<HillfortModel> {
        return user.hillforts
    }

    override fun createHillfort(user: UserModel, hillfort: HillfortModel) {
        hillfort.id = generateRandomId()
        user.hillforts.add(hillfort)
        serialize()
    }

    override fun deleteHillfort(user: UserModel, hillfort: HillfortModel) {
        user.hillforts.remove(hillfort)
        serialize()
    }

    override fun updateHillfort(user: UserModel, hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = user.hillforts.find { p -> p.id == hillfort.id }
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
            logAllHillforts(user)
            serialize()
        }
    }

    override fun findHillfortById(user: UserModel, id: Long): HillfortModel {
        return user.hillforts.find { hillfortModel: HillfortModel -> hillfortModel.id  == id }!!
    }

    fun logAllHillforts(user: UserModel) {
        user.hillforts.forEach{ info("${it}") }
    }
}