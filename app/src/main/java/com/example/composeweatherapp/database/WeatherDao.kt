package com.example.composeweatherapp.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather")
    fun getAll(): List<Weather>

    @Query("SELECT * FROM weather WHERE name LIKE :find_name")
    fun findByName(find_name: String): Weather?
}