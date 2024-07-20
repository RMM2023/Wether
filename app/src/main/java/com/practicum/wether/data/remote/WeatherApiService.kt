package com.practicum.wether.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast.json")
    fun getForecastWeather(
        @Query("key") key:String,
        @Query("q") city : String,
        @Query("days") days : Int = 3
    ) : Call<WeatherResponse>
}