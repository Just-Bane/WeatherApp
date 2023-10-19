package com.example.composeweatherapp.ui.screens.first

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweatherapp.core.no_data
import com.example.composeweatherapp.repository.WeatherRepository
import com.example.composeweatherapp.retrofit.CurrentWeatherData
import com.example.composeweatherapp.ui.screens.second.HomeViewModel
import com.example.composeweatherapp.usecase.DateUseCase
import com.example.composeweatherapp.usecase.InternetUseCase
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
    private val internetUC: InternetUseCase
) : ViewModel() {
    var weather = mutableStateOf(CurrentWeatherData("", "", "", "", ""))
    val currentDate: String = date.getDate().format(Date())
    var networkStatus = mutableStateOf("")
    var _city: String? = null
    init {
        viewModelScope.launch {
            viewModelScope.launch {
                internetUC.observe().collect {
                    withContext(Dispatchers.Main) {
                        networkStatus.value = it.name
                    }
                }
            }
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

    private fun updatePrefInfo(city: String) {
        _city = city
        weatherRepo.city = city
        weatherRepo.cityUpdate = true
        weatherRepo.locUpdate = false
    }

    sealed class CityScreenState {
        object WriteTheCity : CityScreenState()

        object CorrectCityWritten : CityScreenState()

        object WrongCityWritten : CityScreenState()

        object NoInternet : CityScreenState()
    }

    sealed class CityScreenIntent {
        object GetWeatherIntent : CityScreenIntent()
    }

    val screenState =
        mutableStateOf<CityScreenState>(CityScreenState.WriteTheCity)

    fun proceedIntent(intent: CityScreenIntent, city: String) {
        when (intent) {
            is CityScreenIntent.GetWeatherIntent -> {
                viewModelScope.launch {
                    val weatherData = weatherRepo.getCurrentWeatherCity(city)
                    if (weatherData.name == no_data) {
                        screenState.value = CityScreenState.WrongCityWritten
                    } else if (weatherData.name == "Moscow " + weatherRepo.updateStep) {
                        screenState.value = CityScreenState.WrongCityWritten
                    } else {
                        weather.value = weatherData
                        screenState.value = CityScreenState.CorrectCityWritten
                        updatePrefInfo(city)
                    }
                }
            }
        }
    }
}