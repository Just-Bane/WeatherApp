package com.example.composeweatherapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    @PrimaryKey(true) var key: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "temp") val temp: String,
    @ColumnInfo(name = "speed") val speed: String,
    @ColumnInfo(name = "humidity") val humidity: String,
    @ColumnInfo(name = "description") val description: String,
)
