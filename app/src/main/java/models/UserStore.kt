package models

import com.example.hillfort.models.HillfortModel
import com.example.hillfort.models.UserModel

interface UserStore {
    fun findAll(): List<UserModel>
    fun create(user: UserModel)
    fun update(user: UserModel)
    fun delete(user: UserModel)
    fun findAllHillforts(user: UserModel): List<HillfortModel>
    fun createHillfort(user: UserModel, hillfort: HillfortModel)
    fun updateHillfort(user: UserModel, hillfort: HillfortModel)
    fun deleteHillfort(user: UserModel, hillfort: HillfortModel)
    fun login(password: String, email: String): Boolean
    fun findByEmail(email: String): UserModel?
    fun findHillfortById(user: UserModel, id: Long): HillfortModel?
}