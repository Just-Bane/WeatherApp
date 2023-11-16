package com.example.composeweatherapp.ui.screens.city

sealed class CityScreenState {
    object WriteTheCity : CityScreenState()

    object CorrectCityWritten : CityScreenState()

    object WrongCityWritten : CityScreenState()

    object NoInternet : CityScreenState()
}

sealed class CityScreenIntent {
    object GetWeatherIntent : CityScreenIntent()
    object LostInternetIntent: CityScreenIntent()
}

sealed class CityScreenEvents {
    object NavigateToInternetScreen: CityScreenEvents()
}