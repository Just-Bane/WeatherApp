package com.example.composeweatherapp.ui.screens.first

import com.example.composeweatherapp.retrofit.CurrentWeatherData

sealed class CityScreenState {
    object WriteTheCity : CityScreenState()

    class CorrectCityWritten(weather: CurrentWeatherData) : CityScreenState()

    object WrongCityWritten : CityScreenState()

    object NoInternet : CityScreenState()
}

sealed class CityScreenIntent {
    object GetWeatherIntent : CityScreenIntent()
}

enum class CityScreenEvents {
    SOME_EVENT
}