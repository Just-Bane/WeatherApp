package com.example.composeweatherapp.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String,
    ): Response<WeatherData>

    @GET("weather")
    suspend fun getCurrentWeatherCity(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String,
    ): Response<WeatherData>
}