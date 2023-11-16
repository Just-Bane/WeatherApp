package com.example.composeweatherapp.ui.screens.internet

sealed class InternetScreenState {
    object Default: InternetScreenState()
    object FalseInternet: InternetScreenState()
    object TrueInternet: InternetScreenState()
}

sealed class InternetScreenIntent {
    object CheckTheInternetIntent: InternetScreenIntent()
}

sealed class InternetScreenEvents {
    object NavigateToHomeScreen: InternetScreenEvents()
}