package com.example.composeweatherapp.presentation.screens.city

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
class CityViewModel @Inject constructor(
    private val date: DateUseCase,
    private val weatherRepo: WeatherRepository,
    private val internetRepo: InternetRepository
) : BaseViewModel<CityScreenState, CityScreenEvents, CityScreenIntent>(), InternetRepository.ISubscription {
    var weather = mutableStateOf(CurrentWeatherData("", "", "", "", ""))
    val currentDate: String = date.getDate().format(Date())

    var cityFromUI: String? = null

    override val defaultState: CityScreenState = CityScreenState.WriteTheCity

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
                proceedIntent(CityScreenIntent.LostInternetIntent)
            }
            ConnectivityObserver.Status.Losing -> {}
            ConnectivityObserver.Status.Lost -> {
                proceedIntent(CityScreenIntent.LostInternetIntent)
            }
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
                        _viewState.value = CityScreenState.CorrectCityWritten
                        updatePrefInfo(cityFromUI!!)
                    }
                }
            }
            CityScreenIntent.LostInternetIntent -> {
                _viewState.value = CityScreenState.NoInternet
                _event.value = CityScreenEvents.NavigateToInternetScreen
            }
        }
    }

    private fun updatePrefInfo(city: String) {
        cityFromUI = city
        weatherRepo.city = city
        weatherRepo.cityUpdate = true
        weatherRepo.locUpdate = false
    }

    override fun onCleared() {
        super.onCleared()
        internetRepo.unsubscribeInetState(this)
    }
}
