package com.example.composeweatherapp.domain.usecase

import com.example.composeweatherapp.data.network.WeatherData
import com.example.composeweatherapp.domain.params.CurrentWeatherParams
import com.example.composeweatherapp.domain.repository.weather.WeatherRepository
import retrofit2.Response
import javax.inject.Inject

//Щось типу такого
//class GetCurrentWeatherUseCase @Inject constructor(
//    private val weatherRepository: WeatherRepository
//): BaseUseCase<CurrentWeatherParams, Response<WeatherData>>() {
//
//    override suspend fun getSuspend(params: CurrentWeatherParams): Response<WeatherData> {
//        return weatherRepository.getCurrentWeather(params.lat, params.lon)
//    }
//}