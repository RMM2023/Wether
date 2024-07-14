package com.practicum.wether.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current.json")
    fun getCurrentWeather(
        @Query("key") key:String,
        @Query("q") city : String
    ) : Call<WeatherResponse>
}