package models

import android.content.Context
import android.graphics.Bitmap
import com.example.hillfort.helpers.readImageFromPath
import com.example.hillfort.models.UserModel
import org.jetbrains.anko.AnkoLogger
import com.example.hillfort.models.HillfortModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import org.jetbrains.anko.info
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

lateinit var userId: String
lateinit var db: DatabaseReference
lateinit var st: StorageReference

fun generateRandomId(): String {
    return Random().toString()
}

class UserFireStore// When created see if json file exists and load it
    (val context: Context) : UserStore, AnkoLogger {

    init {
        fetchUsers{}
    }

    var users = mutableListOf<UserModel>()
    var imageIndex = 0


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

    override fun findByEmail(email: String?): UserModel? {
        return users.find { user -> user.email == email }
    }

    override fun findById(id: String): UserModel? {
        return users.find { user -> user.id == id }
    }

    override fun create(user: UserModel) {
        user.id = generateRandomId()
        users.add(user)
        info(user)
        val key = db.child("users").push().key
        key?.let {
            user.fbId = key
            users.add(user)
            db.child("users").child(key).setValue(user)
        }
    }

    override fun delete(user: UserModel) {
        users.remove(user)
        db.child("users").child(user.fbId).removeValue()
    }

    override fun update(user: UserModel) {
        val foundUser: UserModel? = users.find { p -> p.id == user.id }
        if (foundUser != null) {
            foundUser.email = user.email
            foundUser.password = user.password
            foundUser.id = user.id
            db.child("users").child(user.fbId).setValue(user)
        }
    }

    fun logAll() {
        users.forEach{ info("${it}") }
    }

    override fun findAllHillforts(user: UserModel): MutableList<HillfortModel> {
        return user.hillforts
    }

    override fun createHillfort(user: UserModel, hillfort: HillfortModel) {
        hillfort.id = generateRandomId()
        val key = db.child("users").child(user.fbId).child("hillforts").push().key
        key?.let {
            hillfort.fbId = key
            user.hillforts.add(hillfort)
            db.child("users").child(user.fbId).child("hillforts").setValue(user.hillforts)
        }
        uploadImage(user, hillfort)
    }

    override fun deleteHillfort(user: UserModel, hillfort: HillfortModel) {
        user.hillforts.remove(hillfort)
        db.child("users").child(user.fbId).child("hillForts").orderByChild("id").equalTo(hillfort.id.toString()).ref.removeValue()
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
            db.child("users").child(user.fbId).child("hillforts").setValue(user.hillforts)
            uploadImage(user, hillfort)
        }
    }

    override fun findHillfortById(user: UserModel, id: String): HillfortModel {
        return user.hillforts.find { hillfortModel: HillfortModel -> hillfortModel.id  == id }!!
    }

    private fun logAllHillforts(user: UserModel) {
        user.hillforts.forEach{ info("${it}") }
    }

    fun fetchUsers(usersReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(users) { it.getValue<UserModel>(UserModel::class.java) }
                usersReady()
            }
        }
        st = FirebaseStorage.getInstance().reference
        db = FirebaseDatabase.getInstance().reference
        db.child("users").addListenerForSingleValueEvent(valueEventListener)
    }

    fun uploadImage(user: UserModel, hillfort: HillfortModel) {
        var hillfortLocation = -1
        user.hillforts.forEachIndexed hillfortFind@ { index, thisHillfort ->
            if(hillfort == thisHillfort){
                hillfortLocation = index
                return@hillfortFind
            }
        }
        if(hillfortLocation == -1){
            return
        }
        hillfort.image.forEachIndexed { index: Int, it: String ->
            if (it  != "" || it != "content://") {
                val fileName = File(it)
                val imageName = fileName.getName()
                var imageRef = st.child(user.fbId + '/' + imageName)
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, it)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        info(it.message)
                    }.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            db.child("users").child(user.fbId).child("hillforts").child(hillfortLocation.toString()).child("image").child(index.toString()).setValue(it.toString())
                        }
                    }
                }
            }
        }
    }
}