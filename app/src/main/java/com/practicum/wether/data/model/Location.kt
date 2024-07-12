package com.practicum.wether.data.model

data class Location(
    val name : String,
    val region : String,
    val country : String,
    val lat : Double,
    val lon : Double,
    val tz_id : String,
    val localtime_epoch : Long,
    val localtime : String
)
