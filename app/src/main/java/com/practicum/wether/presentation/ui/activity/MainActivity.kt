package com.practicum.wether.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.practicum.wether.R
import com.practicum.wether.WEATHER_KEY
import com.practicum.wether.data.remote.WeatherResponse
import com.practicum.wether.data.repository.WeatherRepository
import com.practicum.wether.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageSettings.setOnClickListener{
            val startIntent = Intent(this, Settings :: class.java)
            startActivity(startIntent)
        }
    }
    private fun fetchWeatherData(city : String){
        WeatherRepository.getWeather(WEATHER_KEY, city).enqueue(object : Callback<WeatherResponse>{
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        updateUI(it)//функция обновления
                    }
                }else{
                    Toast.makeText(this@MainActivity, "Не удалось получить данные", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Нет подключения к сети", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun updateUI(weatherResponse : WeatherResponse){
        val location = weatherResponse.location
        val current = weatherResponse.current
        binding.nameCity.text = location.name
        binding.date.text = location.localtime.split(" ")[0]
        binding.time.text = location.localtime.split(" ")[1]
        binding.degree.text = "${current.temp_c}°C"
        binding.textWind.text = "${current.wind_kph} км/ч"
        binding.textPressure.text = "${current.pressure_mb} гПа"
        binding.textHumidity.text = "${current.humidity} %"
        binding.description.text = current.condition.text
    }
}