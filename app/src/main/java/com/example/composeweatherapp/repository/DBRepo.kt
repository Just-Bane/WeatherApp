package com.example.composeweatherapp.repository

import android.content.Context
import androidx.room.Room
import com.example.composeweatherapp.database.WeatherDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DBRepo(private val context: Context) {
    private var db: WeatherDataBase? = null
    suspend fun init() = withContext(Dispatchers.IO) {
        db = Room.databaseBuilder(
            context,
            WeatherDataBase::class.java, "weather"
        ).build()
    }

    fun getDB(): WeatherDataBase {
        return db?: throw DoNotInitializeException()
    }

    class DoNotInitializeException: Exception("Must be initialized")
}