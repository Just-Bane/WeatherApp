package com.example.composeweatherapp.ui.screens.second

import android.content.Context
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
class HomeViewModel @Inject constructor(
    private val date: DateUseCase,
    private val internet: InternetUseCase,
    private val weatherRepo: WeatherRepository
): ViewModel() {

    var weather = mutableStateOf(CurrentWeatherData("", "", "", "", ""))
    val currentDate: String = date.getDate()
    val screenState = mutableStateOf<HomeScreenState>(HomeScreenState.Default)

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

    fun isOnline(): Boolean {
        return internet.isOnline()
    }

    sealed class HomeScreenState {
        object Default: HomeScreenState()
        object NoInternet: HomeScreenState()
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