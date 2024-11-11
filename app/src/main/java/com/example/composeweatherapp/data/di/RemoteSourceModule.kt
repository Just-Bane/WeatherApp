package com.example.composeweatherapp.data.di

import com.dotcode.marble.data.network.source.RemoteDataSource
import com.dotcode.marble.data.network.source.RemoteDataSourceHelper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteSourceModule {
    @Provides
    @Singleton
    fun provideRemoteDataSource(
        remoteDataSource: RemoteDataSource,
        gson: Gson,
    ): RemoteDataSourceHelper =
        RemoteDataSource(
            gson
        )
}