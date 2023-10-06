package com.example.composeweatherapp.ui.screens.first

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweatherapp.repository.WeatherRepository
import com.example.composeweatherapp.retrofit.CurrentWeatherData
import com.example.composeweatherapp.ui.screens.second.HomeViewModel
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
    private val weatherRepo: WeatherRepository
) : ViewModel() {
    var weather = mutableStateOf(CurrentWeatherData("", "", "", "", ""))
    val currentDate: String = date.getDate().format(Date())

    var _city: String? = null
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
                    if (weatherData.name == "NO_DATA") {
                        screenState.value = CityScreenState.WrongCityWritten
                    } else if (weatherData.name == "Moscow "+weatherRepo.updateStep) {
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