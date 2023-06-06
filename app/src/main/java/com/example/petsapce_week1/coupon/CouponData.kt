package com.example.petsapce_week1

import java.io.Serializable

data class CouponData(
    val imgList: Int,
    val location: String,
    val name: String,
    val detail: String,
    val price: Int,
    val dayLeft: Int
): Serializable
