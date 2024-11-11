package com.example.composeweatherapp.presentation.screens.main

import androidx.annotation.DrawableRes

data class WeatherUIData(
    val title: String,
    @DrawableRes val iconId: Int,
    val weatherData: String
)
