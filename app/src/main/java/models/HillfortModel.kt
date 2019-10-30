package com.example.hillfort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HillfortModel(var id: Long = 0,
                         var title: String = "",
                         var image: ArrayList<String> = ArrayList(),
                         var description: String = "",
                         var visited: Boolean = false,
                         var dateVisited: String = "",
                         var notes: ArrayList<String> = ArrayList(),
                         var location: Location = Location()) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable