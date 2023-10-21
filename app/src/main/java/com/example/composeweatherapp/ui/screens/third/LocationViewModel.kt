package com.example.composeweatherapp.ui.screens.third

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.composeweatherapp.core.BaseViewModel
import com.example.composeweatherapp.core.SomeEvent
import com.example.composeweatherapp.core.internet_available
import com.example.composeweatherapp.core.internet_lost
import com.example.composeweatherapp.repository.internet.InternetRepository
import com.example.composeweatherapp.repository.weather.WeatherRepository
import com.example.composeweatherapp.retrofit.CurrentWeatherData
import com.example.composeweatherapp.usecase.DateUseCase
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
) : BaseViewModel<LocationScreenState, SomeEvent<LocationScreenEvent>, LocationScreenIntent>() {

    var weather = mutableStateOf(CurrentWeatherData("", "", "", "", ""))
    val currentDate: String = date.getDate().format(Date())

    var latFromUI: String? = null
    var lonFromUI: String? = null

    override val defaultState: LocationScreenState = LocationScreenState.WriteTheLocation

    init {
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

    fun isOnlineChecking() {
        if (internetRepo.isOnline()) {
            _viewState.value = LocationScreenState.WriteTheLocation
        } else {
            _viewState.value = LocationScreenState.NoInternet
        }

        if (internetRepo.networkStatusObserver.value == internet_available) {
            _viewState.value = LocationScreenState.WriteTheLocation
        } else if (internetRepo.networkStatusObserver.value == internet_lost) {
            _viewState.value = LocationScreenState.NoInternet
        }
    }

    fun changeToDefaultState() {
        _viewState.value = defaultState
    }

    override fun proceedIntent(intent: LocationScreenIntent) {
        when (intent) {
            LocationScreenIntent.GetTheWeatherIntent -> {
                viewModelScope.launch {
                    val weatherData = weatherRepo.getCurrentWeather(latFromUI!!, lonFromUI!!)
                    if (weatherData.name == "NO_DATA") {
                        _viewState.value = LocationScreenState.WrongLocationWritten
                    } else {
                        weather.value = weatherData
                        _viewState.value = LocationScreenState.CorrectLocationWritten
                        updatePrefLoc(latFromUI!!, lonFromUI!!)
                    }
                }
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
}