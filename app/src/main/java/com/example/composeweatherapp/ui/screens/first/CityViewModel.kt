package com.example.composeweatherapp.ui.screens.first

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.composeweatherapp.core.BaseViewModel
import com.example.composeweatherapp.core.SomeEvent
import com.example.composeweatherapp.core.internet_available
import com.example.composeweatherapp.core.internet_lost
import com.example.composeweatherapp.core.no_data
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
class CityViewModel @Inject constructor(
    private val date: DateUseCase,
    private val weatherRepo: WeatherRepository,
    private val internetRepo: InternetRepository
) : BaseViewModel<CityScreenState, SomeEvent<CityScreenEvents>, CityScreenIntent>() {
    var weather = mutableStateOf(CurrentWeatherData("", "", "", "", ""))
    val currentDate: String = date.getDate().format(Date())

    var cityFromUI: String? = null

    override val defaultState: CityScreenState = CityScreenState.WriteTheCity

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
            _viewState.value = CityScreenState.WriteTheCity
        } else {
            _viewState.value = CityScreenState.NoInternet
        }

        if (internetRepo.networkStatusObserver.value == internet_available) {
            _viewState.value = CityScreenState.WriteTheCity
        } else if (internetRepo.networkStatusObserver.value == internet_lost) {
            _viewState.value = CityScreenState.NoInternet
        }
    }

    fun changeToDefaultState() {
        _viewState.value = defaultState
    }

    override fun proceedIntent(intent: CityScreenIntent) {
        when (intent) {
            is CityScreenIntent.GetWeatherIntent -> {
                viewModelScope.launch {
                    val weatherData = weatherRepo.getCurrentWeatherCity(cityFromUI!!)
                    if (weatherData.name == no_data) {
                        _viewState.value = CityScreenState.WrongCityWritten
                    } else {
                        weather.value = weatherData
                        _viewState.value = CityScreenState.CorrectCityWritten(weatherData)
                        updatePrefInfo(cityFromUI!!)
                    }
                }
            }
        }
    }

    private fun updatePrefInfo(city: String) {
        cityFromUI = city
        weatherRepo.city = city
        weatherRepo.cityUpdate = true
        weatherRepo.locUpdate = false
    }
}
