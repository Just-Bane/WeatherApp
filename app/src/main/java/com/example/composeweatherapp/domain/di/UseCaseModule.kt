package com.example.composeweatherapp.domain.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    //По принципу інших модулів, прокидувати кожен новий usecase
}