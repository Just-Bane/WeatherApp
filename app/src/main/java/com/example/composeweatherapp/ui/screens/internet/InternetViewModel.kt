package com.example.composeweatherapp.ui.screens.internet

import androidx.compose.runtime.mutableStateOf
import com.example.composeweatherapp.core.BaseViewModel
import com.example.composeweatherapp.core.SomeEvent
import com.example.composeweatherapp.repository.internet.InternetRepository
import com.example.composeweatherapp.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InternetViewModel @Inject constructor(
    private val internetRepo: InternetRepository,
    private val weatherRepo: WeatherRepository
): BaseViewModel<InternetScreenState, SomeEvent<InternetScreenEvent>, InternetScreenIntent>() {

    var weatherRefreshed = mutableStateOf(false)

    override val defaultState: InternetScreenState = InternetScreenState.Default

    override fun proceedIntent(intent: InternetScreenIntent) {
        when (intent) {
            InternetScreenIntent.DefaultIntent -> {
                if (internetRepo.isOnline()) {
                    onWeatherRefresh()
                    if (weatherRefreshed.value) {
                        _viewState.value = InternetScreenState.TrueInternet
                    }
                } else {
                    _viewState.value = InternetScreenState.FalseInternet
                }
            }
        }
    }

    private fun onWeatherRefresh() {
        weatherRepo.onWeatherRefresh()
        weatherRefreshed.value = true
    }
}