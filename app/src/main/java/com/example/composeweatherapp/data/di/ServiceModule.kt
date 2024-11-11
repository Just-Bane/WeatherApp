package com.example.composeweatherapp.data.di

import android.content.Context
import android.util.Log
import com.dotcode.marble.data.network.source.RemoteDataSource
import com.example.composeweatherapp.data.network.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {
    @Provides
    @Singleton
    fun provideWeatherService(
        remoteDataSource: RemoteDataSource,
        @ApplicationContext context: Context
    ): WeatherService {
        return remoteDataSource.buildApi(
            WeatherService::class.java,
            //Ось цю строку треба мувнути в окремий клас Constants в пакеті common
            "https://api.openweathermap.org/data/2.5/"
        )
    }
}