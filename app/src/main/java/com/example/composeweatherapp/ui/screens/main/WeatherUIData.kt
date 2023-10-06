package com.example.composeweatherapp.ui.screens.main

import androidx.annotation.DrawableRes

data class WeatherUIData(
    val title: String,
    @DrawableRes val iconId: Int,
    val weatherData: String
)
