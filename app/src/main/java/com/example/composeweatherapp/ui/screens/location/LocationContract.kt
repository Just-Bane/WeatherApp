package com.example.composeweatherapp.ui.screens.location

sealed class LocationScreenState {
    object WriteTheLocation : LocationScreenState()
    object CorrectLocationWritten : LocationScreenState()
    object WrongLocationWritten : LocationScreenState()
    object NoInternet : LocationScreenState()
}

sealed class LocationScreenIntent {
    object GetWeatherIntent: LocationScreenIntent()
    object LostInternetIntent: LocationScreenIntent()
}

sealed class LocationScreenEvents {
    object NavigateToInternetScreen: LocationScreenEvents()
}