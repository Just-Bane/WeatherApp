package com.example.composeweatherapp.repository

import android.util.Log
import com.example.composeweatherapp.retrofit.CurrentWeatherData
import com.example.composeweatherapp.retrofit.RetrofitInit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class WeatherRepository @Inject constructor(
    private val retrofit: RetrofitInit
): CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO

    private val apiKey = "d3e58f1e479b38f706f22be4e42a2f68"


    suspend fun getCurrentWeather(
        lat: String,
        lon: String
    ): CurrentWeatherData = withContext(Dispatchers.IO) {
        val response = retrofit.api.getCurrentWeather(
            lat = lat,
            lon = lon,
            appid = apiKey
        )
        return@withContext response.body()?.let { data ->
            CurrentWeatherData(
                temp = Math.round((data.main.temp).toDouble()).toString(),
                name = data.name,
                humidity = data.main.humidity,
                description = data.weather[0].description,
                speed = data.wind.speed,

            )
        } ?: CurrentWeatherData(
            name = "NO_DATA",
            temp = "NO_DATA",
            humidity = "NO_DATA",
            description = "NO_DATA",
            speed = "NO_DATA"
        )
    }
}