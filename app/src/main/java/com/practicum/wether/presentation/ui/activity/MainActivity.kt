package com.practicum.wether.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.practicum.wether.R
import com.practicum.wether.WEATHER_KEY
import com.practicum.wether.data.model.Forecast
import com.practicum.wether.data.remote.WeatherResponse
import com.practicum.wether.data.repository.WeatherRepository
import com.practicum.wether.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.contracts.contract

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private var handler = Handler(Looper.getMainLooper())
    private var runnable : Runnable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cityEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                runnable?.let { handler.removeCallbacks(it)}
                runnable = Runnable {
                    val city = s.toString()
                    if (city.isNotEmpty()){
                        fetchWeatherData(city)
                    }
                }
                handler.postDelayed(runnable!!, 1000)
            }
        })
        val lastCity = getLastSearchedCity()
        if (lastCity.isNotEmpty()){
            fetchWeatherData(lastCity)
        }else{
            fetchWeatherData("Moscow")
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
                        updateUI(it)
                        updateForecastUI(it.forecast)
                        saveLastSearchedCity(city)
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

    private fun updateForecastUI(forecast: Forecast) {
        val days = listOf(binding.day1, binding.day2, binding.day3)
        val dates = listOf(binding.textDate1, binding.textDate2, binding.textDate3)
        val icons = listOf(binding.iconDay1, binding.iconDay2, binding.iconDay3)
        val degrees = listOf(binding.degreeDay1, binding.degreeDay2, binding.degreeDay3)

        forecast.forecastday.take(3).forEachIndexed { index, forecastDay ->
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(forecastDay.date)
            val dayOfWeek = SimpleDateFormat("EEEE", Locale("ru")).format(date)

            days[index].text = dayOfWeek
            dates[index].text = forecastDay.date

            val iconUrl = "https:${forecastDay.day.condition.icon}"
            Glide.with(this).load(iconUrl).into(icons[index])

            degrees[index].text = "${forecastDay.day.maxtemp_c}°C/${forecastDay.day.mintemp_c}°C"
        }
    }
    private fun updateUI(weatherResponse : WeatherResponse){
        val location = weatherResponse.location
        val current = weatherResponse.current
        val forecast = weatherResponse.forecast.forecastday.firstOrNull()
        val iconUrl = "https:${current.condition.icon}"
        Glide.with(this).load(iconUrl).into(binding.mainIcon)
        binding.nameCity.text = location.name
        binding.date.text = location.localtime.split(" ")[0]
        binding.time.text = location.localtime.split(" ")[1]
        binding.degree.text = "${current.temp_c}°C"
        binding.textWind.text = "${current.wind_kph} км/ч"
        binding.textPressure.text = "${current.pressure_mb} гПа"
        binding.textHumidity.text = "${current.humidity} %"
        binding.description.text = current.condition.text
        forecast?.astro?.let { astro ->
            binding.sunrise.text = "Восход: ${astro.sunrise}"
            binding.sunset.text = "Закат: ${astro.sunset}"
        }

    }
    fun saveLastSearchedCity(city: String){
        val sharedPreferences = getSharedPreferences("prefKey", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit().putString("keyCity", city)
        editor.apply()
    }
    fun getLastSearchedCity() : String{
        val sharedPreferences = getSharedPreferences("prefKey", Context.MODE_PRIVATE)
        val city : String = sharedPreferences.getString("keyCity", "")?:""
        return city
    }
}
