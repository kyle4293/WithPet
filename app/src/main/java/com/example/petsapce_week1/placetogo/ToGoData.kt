package com.example.petsapce_week1.placetogo

import com.example.petsapce_week1.home.Home2ChildData
import java.io.Serializable

data class ToGoData(
    val imgList: Int,
    val name: String,
    val location:String,
    val score: Double
): Serializable