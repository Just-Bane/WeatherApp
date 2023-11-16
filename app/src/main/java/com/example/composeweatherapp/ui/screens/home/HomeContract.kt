package com.example.composeweatherapp.ui.screens.home

sealed class HomeScreenState {
    object Default: HomeScreenState()
    object NoInternet: HomeScreenState()
}

sealed class HomeScreenIntent {
    object GetWeatherIntent : HomeScreenIntent()
    object LostInternetIntent : HomeScreenIntent()
}

sealed class HomeScreenEvents {
    object NavigateToInternetScreen: HomeScreenEvents()
}