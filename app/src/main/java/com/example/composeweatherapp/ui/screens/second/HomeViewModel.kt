package com.example.composeweatherapp.ui.screens.second

import android.util.Log
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val date: DateUseCase,
    private val weatherRepo: WeatherRepository,
    private val internetRepo: InternetRepository
): BaseViewModel<HomeScreenState, SomeEvent<HomeScreenEvent>, HomeScreenIntent>() {

    var weather = mutableStateOf(CurrentWeatherData("", "", "", "", ""))
    val currentDate: String = date.getDate()

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


    override val defaultState: HomeScreenState = HomeScreenState.Default

    override fun proceedIntent(intent: HomeScreenIntent) {
        when(intent) {
            HomeScreenIntent.GetWeatherIntent -> {
                if (internetRepo.isOnline()) {
                    _viewState.value = HomeScreenState.Default
                } else {
                    Log.e("fix", "Its no internet(from isOnline)")
                    _viewState.value = HomeScreenState.NoInternet
                }
                if (internetRepo.networkStatusObserver.value == internet_lost) {
                    _viewState.value = HomeScreenState.NoInternet
                }
            }
        }
    }











//    sealed class SecondScreenState {
//        // Введите пароль
//        data class PrintPassword(
//            val text: String = "Enter a password",
//            val button: String = "Login"
//        ) : SecondScreenState()
//        // Правильный ввод пароля
//        data class CorrectPassword(
//            val text: String = "Loading..."
//        ) : SecondScreenState()
//        // Неправильный ввод пароля
//        data class WrongPassword(
//            val text: String = "Password is not correct. Try again",
//            val button: String = "Login"
//        ) : SecondScreenState()
//        // Ошибка
//        data class Error(
//            val text: Exception = Exception("Unknown exception"),
//            val button: String = "Retry"
//        ) : SecondScreenState()
//        // Нет интернета
//        object NoInternet: SecondScreenState()
//        // Залогинены
//        data class Logged(
//            val text: String = "Some content"
//        ): SecondScreenState()
//    }
//
//    sealed class SecondScreenIntent {
//        // Нажатие кнопки -> Залогиниться
//        data class LoginIntent(
//            val login: String,
//            val password: String
//        ): SecondScreenIntent()
//        // Повторить операцию
//        object RetryIntent: SecondScreenIntent()
//        // Проверить интернет и повторить
//        object CheckAndRetryIntent: SecondScreenIntent()
//    }
//
//    val screenState = mutableStateOf<SecondScreenState>(SecondScreenState.PrintPassword())
//
//    fun proceedIntent(intent: SecondScreenIntent) {
//        when(intent) {
//            SecondScreenIntent.CheckAndRetryIntent -> {
//                screenState.value = SecondScreenState.WrongPassword()
//            }
//            is SecondScreenIntent.LoginIntent -> {
//                // smth
//            }
//            SecondScreenIntent.RetryIntent -> {
//                // smth
//            }
//        }
//    }
}