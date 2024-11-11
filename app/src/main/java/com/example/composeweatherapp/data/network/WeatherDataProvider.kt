package com.example.composeweatherapp.data.network

import retrofit2.Response
import javax.inject.Inject

class WeatherDataProvider @Inject constructor(
    private val weatherService: WeatherService
): WeatherHelper {
    override suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        units: String,
        appid: String
    ): Response<WeatherData> {
        return weatherService.getCurrentWeather(lat = lat, lon = lon, units = units, appid = appid)
    }

    override suspend fun getCurrentWeatherCity(
        city: String,
        units: String,
        appid: String
    ): Response<WeatherData> {
        return weatherService.getCurrentWeatherCity(city, units, appid)
    }
}