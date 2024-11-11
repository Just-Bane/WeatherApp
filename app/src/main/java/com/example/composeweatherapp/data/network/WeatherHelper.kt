package com.example.composeweatherapp.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherHelper {
    suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        units: String = "metric",
        appid: String,
    ): Response<WeatherData>

    suspend fun getCurrentWeatherCity(
        city: String,
        units: String = "metric",
        appid: String,
    ): Response<WeatherData>
}