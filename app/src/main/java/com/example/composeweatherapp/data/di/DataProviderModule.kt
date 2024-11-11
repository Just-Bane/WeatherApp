package com.example.composeweatherapp.data.di

import com.example.composeweatherapp.data.network.WeatherDataProvider
import com.example.composeweatherapp.data.network.WeatherHelper
import com.example.composeweatherapp.data.network.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataProviderModule {
    @Provides
    @Singleton
    fun provideAuthenticationControllerDataProvider(
        weatherService: WeatherService
    ): WeatherHelper =
        WeatherDataProvider(weatherService)
}