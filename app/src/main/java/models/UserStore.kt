package models

import com.example.hillfort.models.HillfortModel
import com.example.hillfort.models.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserStore {
    fun findAll(): List<UserModel>
    fun create(user: UserModel)
    fun update(user: UserModel)
    fun delete(user: UserModel)
    fun findById(id: String): UserModel?
    fun findAllHillforts(user: UserModel): List<HillfortModel>
    fun createHillfort(user: UserModel, hillfort: HillfortModel)
    fun updateHillfort(user: UserModel, hillfort: HillfortModel)
    fun deleteHillfort(user: UserModel, hillfort: HillfortModel)
    fun login(email: String, password: String): Boolean
    fun findByEmail(email: String?): UserModel?
    fun findHillfortById(user: UserModel, id: String): HillfortModel?
}