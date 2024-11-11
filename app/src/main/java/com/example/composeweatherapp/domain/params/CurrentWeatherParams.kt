package com.example.composeweatherapp.domain.params


data class CurrentWeatherParams(
    val lat: String,
    val lon: String,
    val units: String,
    val appid: String,
)