package com.example.hillfort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HillfortModel(var id: String = "",
                         var fbId : String = "",
                         var title: String = "",
                         var image: ArrayList<String> = ArrayList(),
                         var description: String = "",
                         var visited: Boolean = false,
                         var dateVisited: String = "",
                         var notes: String = "",
                         var location: Location = Location()) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable