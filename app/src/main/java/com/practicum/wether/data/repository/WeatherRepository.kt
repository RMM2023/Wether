package com.practicum.wether.data.repository

import com.practicum.wether.data.remote.WeatherApiService
import com.practicum.wether.data.remote.WeatherResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRepository {
    val apiService : WeatherApiService
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(WeatherApiService::class.java)
    }
    fun getWeather(key : String, city : String) : Call<WeatherResponse>{
        return apiService.getCurrentWeather(key, city)
    }
}