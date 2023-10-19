package com.example.composeweatherapp.ui.screens.third

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweatherapp.repository.WeatherRepository
import com.example.composeweatherapp.retrofit.CurrentWeatherData
import com.example.composeweatherapp.usecase.DateUseCase
import com.example.composeweatherapp.usecase.InternetUseCase
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
    private val internetUC: InternetUseCase
) : ViewModel() {
    var weather = mutableStateOf(CurrentWeatherData("", "", "", "", ""))
    val currentDate: String = date.getDate().format(Date())
    var networkStatus = mutableStateOf("")

    var _lat: String? = null
    var _lon: String? = null

    init {
        viewModelScope.launch {
            internetUC.observe().collect {
                withContext(Dispatchers.Main) {
                    networkStatus.value = it.name
                }
            }
        }
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

    fun isOnline(): Boolean { return internetUC.isOnline() }

    sealed class LocationScreenState {
        object WriteTheLocation : LocationScreenState()
        object CorrectLocationWritten : LocationScreenState()
        object WrongLocationWritten : LocationScreenState()
        object NoInternet : LocationScreenState()
    }

    sealed class LocationScreenIntent {
        object GetTheWeatherIntent: LocationScreenIntent()
    }

    val screenState = mutableStateOf<LocationScreenState>(LocationScreenState.WriteTheLocation)

    fun proceedIntent(intent: LocationScreenIntent, lat: String, lon: String) {
        when (intent) {
            LocationScreenIntent.GetTheWeatherIntent -> {
                viewModelScope.launch {
                    val weatherData = weatherRepo.getCurrentWeather(lat, lon)
                    if (weatherData.name == "NO_DATA") {
                        screenState.value = LocationScreenState.WrongLocationWritten
                    } else {
                        weather.value = weatherData
                        screenState.value = LocationScreenState.CorrectLocationWritten
                        updatePrefLoc(lat, lon)
                    }
                }
            }
        }
    }

    private fun updatePrefLoc(lat: String, lon: String) {
        _lat = lat
        _lon = lon
        weatherRepo.lat = lat
        weatherRepo.lon = lon
        weatherRepo.locUpdate = true
        weatherRepo.cityUpdate = false
    }
}