package com.example.composeweatherapp.data.network

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("name")
    val name: String,
    @SerializedName("main")
    val main: WeatherMain,
    @SerializedName("wind")
    val wind: WeatherWind,
    @SerializedName("weather")
    val weather: ArrayList<WeatherWeather>
)

data class WeatherMain(
    val temp: String,
    val humidity: String
)

data class WeatherWind(
    val speed: String
)

data class WeatherWeather(
    val description: String
)

data class CurrentWeatherData(
    val name: String,
    val temp: String,
    val speed: String,
    val humidity: String,
    val description: String
)