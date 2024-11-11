package com.example.composeweatherapp.presentation.screens.internet

import androidx.compose.runtime.mutableStateOf
import com.example.composeweatherapp.common.core.BaseViewModel
import com.example.composeweatherapp.common.core.SomeEvent
import com.example.composeweatherapp.domain.repository.internet.ConnectivityObserver
import com.example.composeweatherapp.domain.repository.internet.InternetRepository
import com.example.composeweatherapp.domain.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InternetViewModel @Inject constructor(
    private val internetRepo: InternetRepository,
    private val weatherRepo: WeatherRepository
) : BaseViewModel<InternetScreenState, InternetScreenEvents, InternetScreenIntent>(),
    InternetRepository.ISubscription {

    private var isInternetAvailable: Boolean = false

    var weatherRefreshed = mutableStateOf(false)

    override val defaultState: InternetScreenState = InternetScreenState.Default

    init {
        _viewState.value = defaultState

        internetRepo.subscribeOnInetState(this)
    }

    override fun onStatusChanged(status: ConnectivityObserver.Status) {
        when (status) {
            ConnectivityObserver.Status.Available -> {
                isInternetAvailable = true
            }
            ConnectivityObserver.Status.Unavailable -> {}
            ConnectivityObserver.Status.Losing -> {}
            ConnectivityObserver.Status.Lost -> {}
        }
    }

    override fun proceedIntent(intent: InternetScreenIntent) {
        when (intent) {
            InternetScreenIntent.CheckTheInternetIntent -> {
                if (isInternetAvailable) {
                    onWeatherRefresh()
                    if (weatherRefreshed.value) {
                        _viewState.value = InternetScreenState.TrueInternet
                        _event.value = InternetScreenEvents.NavigateToHomeScreen
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

    override fun onCleared() {
        super.onCleared()
        internetRepo.unsubscribeInetState(this)
    }
}