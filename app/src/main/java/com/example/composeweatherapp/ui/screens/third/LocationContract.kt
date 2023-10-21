package com.example.composeweatherapp.ui.screens.third

sealed class LocationScreenState {
    object WriteTheLocation : LocationScreenState()
    object CorrectLocationWritten : LocationScreenState()
    object WrongLocationWritten : LocationScreenState()
    object NoInternet : LocationScreenState()
}

sealed class LocationScreenIntent {
    object GetTheWeatherIntent: LocationScreenIntent()
}

enum class LocationScreenEvent {
    SOME_EVENT
}