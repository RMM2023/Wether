package com.practicum.wether.data.remote

import com.practicum.wether.data.model.Current
import com.practicum.wether.data.model.Forecast
import com.practicum.wether.data.model.Location

data class WeatherResponse(
    val location : Location,
    val current: Current,
    val forecast: Forecast
)
