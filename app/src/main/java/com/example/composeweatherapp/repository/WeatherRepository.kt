package com.example.composeweatherapp.repository

import android.util.Log
import com.example.composeweatherapp.retrofit.CurrentWeatherData
import com.example.composeweatherapp.retrofit.RetrofitInit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class WeatherRepository @Inject constructor(
    private val retrofit: RetrofitInit
) : CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO

    private val apiKey = "d3e58f1e479b38f706f22be4e42a2f68"

    val weatherUIFlow: MutableStateFlow<CurrentWeatherData?> = MutableStateFlow(null)

    private var data: CurrentWeatherData? = null

    var locationLat: String? = null
    var locationLon: String? = null

    var locUpdate: Boolean = false
    var cityUpdate: Boolean = false

    var lat: String? = null
    var lon: String? = null
    var city: String? = null

    var updateStep = 0

    var locationTimeout: Int = 0

    private var firstDownload: Boolean = true


    init {
        launch {
            while (true) {
                if (firstDownload) {
                    while (locationLat == null) {
                        delay(100)
                        locationTimeout += 1
                        if (locationTimeout == 20) {
                            locationLat = "no_location"
                        }
                    }
                    firstDownload = false
                }
                updateStep++
                if (locationLat == "no_location") {
                    data = CurrentWeatherData(
                        name = "No_location",
                        temp = "-",
                        speed = "-",
                        humidity = "-",
                        description = "no_loc"
                    )
                } else if (city != null && cityUpdate) {
                    data = getCurrentWeatherCity(city!!)
                } else if (lat != null && lon != null && locUpdate) {
                    data = getCurrentWeather(lat!!, lon!!)
                } else {
                    data = getCurrentWeather(locationLat!!, locationLon!!)
                }
                weatherUIFlow.emit(data)
                Log.e("flow", "emited data $data")
                delay(3_000)
            }
        }
    }


    suspend fun getCurrentWeather(
        lat: String,
        lon: String
    ): CurrentWeatherData = withContext(Dispatchers.IO) {
        val response = retrofit.api.getCurrentWeather(
            lat = lat,
            lon = lon,
            appid = apiKey
        )
        return@withContext response.body()?.let { data ->
            CurrentWeatherData(
                temp = Math.round((data.main.temp).toDouble()).toString(),
                name = data.name + " " + updateStep,
                humidity = data.main.humidity,
                description = data.weather[0].description,
                speed = data.wind.speed,

                )
        } ?: CurrentWeatherData(
            name = "NO_DATA",
            temp = "NO_DATA",
            humidity = "NO_DATA",
            description = "NO_DATA",
            speed = "NO_DATA"
        )
    }

    suspend fun getCurrentWeatherCity(
        city: String
    ): CurrentWeatherData = withContext(Dispatchers.IO) {
        val response = retrofit.api.getCurrentWeatherCity(
            city = city,
            appid = apiKey
        )
        return@withContext response.body()?.let { data ->
            CurrentWeatherData(
                temp = Math.round((data.main.temp).toDouble()).toString(),
                name = data.name + " " + updateStep,
                humidity = data.main.humidity,
                description = data.weather[0].description,
                speed = data.wind.speed,

                )
        } ?: CurrentWeatherData(
            name = "NO_DATA",
            temp = "NO_DATA",
            humidity = "NO_DATA",
            description = "NO_DATA",
            speed = "NO_DATA"
        )
    }
}