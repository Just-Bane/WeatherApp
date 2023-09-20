package com.example.composeweatherapp.di

import android.content.Context
import com.example.composeweatherapp.repository.DBRepo
import com.example.composeweatherapp.repository.WeatherRepository
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
    fun provideDBRepo(context: Context): DBRepo {
        return DBRepo(context)
    }

    @Provides
    @Singleton
    fun provideWeatherRepo(retrofit: RetrofitInit): WeatherRepository {
        return WeatherRepository(retrofit)
    }
}