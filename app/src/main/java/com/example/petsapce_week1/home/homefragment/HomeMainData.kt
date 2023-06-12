package com.example.petsapce_week1.home.homefragment

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class HomeMainData(
    val imgList: Int, val name: String, val location: String, val score: Double
):Serializable