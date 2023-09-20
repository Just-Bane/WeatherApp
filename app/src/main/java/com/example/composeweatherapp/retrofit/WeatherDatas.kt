package com.example.composeweatherapp.retrofit

data class WeatherData(
    val name: String,
    val main: WeatherMain,
    val wind: WeatherWind,
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