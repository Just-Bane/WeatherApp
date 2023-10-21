package com.example.composeweatherapp.ui.screens.second

sealed class HomeScreenState {
    object Default: HomeScreenState()
    object NoInternet: HomeScreenState()
}

sealed class HomeScreenIntent {
    object GetWeatherIntent : HomeScreenIntent()
}

enum class HomeScreenEvent {
    SOME_EVENT
}