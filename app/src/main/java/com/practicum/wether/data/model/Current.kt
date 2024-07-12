package com.practicum.wether.data.model

data class Current(
    val temp_c: Double,
    val is_day: Int,
    val condition: Condition,
    val wind_kph: Double,
    val pressure_mb: Double,
    val humidity: Int,
    val cloud: Int,
    val feelslike_c: Double,
    val vis_km: Double,
    val uv: Double,
    val gust_kph: Double
)
