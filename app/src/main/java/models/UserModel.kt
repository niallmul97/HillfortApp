package com.example.hillfort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class UserModel(var id: String = "",
                     var fbId : String = "",
                     var email: String = "",
                     var password: String = "",
                     var hillforts: ArrayList<HillfortModel> = ArrayList()): Parcelable