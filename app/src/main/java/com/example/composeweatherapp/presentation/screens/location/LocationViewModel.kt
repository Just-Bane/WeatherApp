package com.example.composeweatherapp.presentation.screens.location

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.composeweatherapp.common.core.BaseViewModel
import com.example.composeweatherapp.common.core.no_data
import com.example.composeweatherapp.domain.repository.internet.ConnectivityObserver
import com.example.composeweatherapp.domain.repository.internet.InternetRepository
import com.example.composeweatherapp.domain.repository.weather.WeatherRepository
import com.example.composeweatherapp.data.network.CurrentWeatherData
import com.example.composeweatherapp.domain.usecase.DateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val date: DateUseCase,
    private val weatherRepo: WeatherRepository,
    private val internetRepo: InternetRepository
) : BaseViewModel<LocationScreenState, LocationScreenEvents, LocationScreenIntent>(),
    InternetRepository.ISubscription {

    var weather = mutableStateOf(CurrentWeatherData("", "", "", "", ""))
    val currentDate: String = date.getDate().format(Date())

    var latFromUI: String? = null
    var lonFromUI: String? = null

    override val defaultState: LocationScreenState = LocationScreenState.WriteTheLocation

    init {
        _viewState.value = defaultState

        internetRepo.subscribeOnInetState(this)
        viewModelScope.launch {
            weatherRepo.weatherUIFlow.collect {
                withContext(Dispatchers.Main) {
                    if (it != null) {
                        weather.value = it
                    }
                }
            }
        }
    }

    override fun onStatusChanged(status: ConnectivityObserver.Status) {
        when (status) {
            ConnectivityObserver.Status.Available -> {}
            ConnectivityObserver.Status.Unavailable -> {
                proceedIntent(LocationScreenIntent.LostInternetIntent)
            }
            ConnectivityObserver.Status.Losing -> {}
            ConnectivityObserver.Status.Lost -> {
                proceedIntent(LocationScreenIntent.LostInternetIntent)
            }
        }
    }

    fun changeToDefaultState() {
        _viewState.value = defaultState
    }

    override fun proceedIntent(intent: LocationScreenIntent) {
        when (intent) {
            LocationScreenIntent.GetWeatherIntent -> {
                viewModelScope.launch {
                    val weatherData = weatherRepo.getCurrentWeather(latFromUI!!, lonFromUI!!)
                    if (weatherData.name == no_data) {
                        _viewState.value = LocationScreenState.WrongLocationWritten
                    } else {
                        weather.value = weatherData
                        _viewState.value = LocationScreenState.CorrectLocationWritten
                        updatePrefLoc(latFromUI!!, lonFromUI!!)
                    }
                }
            }

            LocationScreenIntent.LostInternetIntent -> {
                _viewState.value = LocationScreenState.NoInternet
                _event.value = LocationScreenEvents.NavigateToInternetScreen
            }
        }
    }

    private fun updatePrefLoc(lat: String, lon: String) {
        latFromUI = lat
        latFromUI = lon
        weatherRepo.lat = lat
        weatherRepo.lon = lon
        weatherRepo.locUpdate = true
        weatherRepo.cityUpdate = false
    }

    override fun onCleared() {
        super.onCleared()
        internetRepo.unsubscribeInetState(this)
    }
}