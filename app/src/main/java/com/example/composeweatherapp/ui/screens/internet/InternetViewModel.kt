package com.example.composeweatherapp.ui.screens.internet

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweatherapp.repository.WeatherRepository
import com.example.composeweatherapp.usecase.InternetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InternetViewModel @Inject constructor(
    private val internetUC: InternetUseCase,
    private val weatherRepo: WeatherRepository
): ViewModel() {

    var weatherRefreshed = mutableStateOf(false)

    fun isOnline(): Boolean {
        return internetUC.isOnline()
    }

    fun onButtonRetryClicked() {
        internetUC.buttonRetryClicked = true
    }

    fun onWeatherRefresh() {
        weatherRepo.onWeatherRefresh()
        weatherRefreshed.value = true
    }
}