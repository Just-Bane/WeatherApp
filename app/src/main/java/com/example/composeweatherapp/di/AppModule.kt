package com.example.composeweatherapp.di

import android.content.Context
import com.example.composeweatherapp.repository.internet.InternetRepository
import com.example.composeweatherapp.repository.weather.WeatherRepository
import com.example.composeweatherapp.retrofit.RetrofitInit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideInternetRepo(context: Context): InternetRepository {
        return InternetRepository(context)
    }

    @Provides
    @Singleton
    fun provideWeatherRepo(retrofit: RetrofitInit, internetRepo: InternetRepository): WeatherRepository {
        return WeatherRepository(retrofit, internetRepo)
    }
}