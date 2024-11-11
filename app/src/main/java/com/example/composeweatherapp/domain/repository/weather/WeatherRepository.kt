package com.example.composeweatherapp.domain.repository.weather

import android.util.Log
import com.example.composeweatherapp.common.core.no_data
import com.example.composeweatherapp.domain.repository.internet.ConnectivityObserver
import com.example.composeweatherapp.domain.repository.internet.InternetRepository
import com.example.composeweatherapp.data.network.CurrentWeatherData
import com.example.composeweatherapp.data.network.RetrofitInit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class WeatherRepository @Inject constructor(
    private val retrofit: RetrofitInit,
    private val internetRepo: InternetRepository
) : CoroutineScope, InternetRepository.ISubscription {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO

    private val apiKey = "d3e58f1e479b38f706f22be4e42a2f68"

    val weatherUIFlow: MutableStateFlow<CurrentWeatherData?> = MutableStateFlow(null)

    private var data: CurrentWeatherData? = null
    private var previousData: CurrentWeatherData? = null

    var locationLat: String? = null
    var locationLon: String? = null

    var locUpdate: Boolean = false
    var cityUpdate: Boolean = false

    var lat: String? = null
    var lon: String? = null
    var city: String? = null

    var updateStep = 0

    private var locationTimeout: Int = 0

    private var firstDownload: Boolean = true
    private var isInternetAvailable: Boolean = false

    override fun onStatusChanged(status: ConnectivityObserver.Status) {
        when (status) {
            ConnectivityObserver.Status.Available -> isInternetAvailable = true
            ConnectivityObserver.Status.Unavailable -> {}
            ConnectivityObserver.Status.Losing -> {}
            ConnectivityObserver.Status.Lost -> isInternetAvailable = false
        }
    }

    init {
        internetRepo.subscribeOnInetState(this)
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
                    if (isInternetAvailable) {
                        data = getCurrentWeatherCity(city!!)
                    } else {
                        previousData = data
                    }
                } else if (lat != null && lon != null && locUpdate) {
                    if (isInternetAvailable) {
                        data = getCurrentWeather(lat!!, lon!!)
                    } else {
                        previousData = data
                    }
                } else {
                    if (isInternetAvailable) {
                        data = getCurrentWeather(locationLat!!, locationLon!!)
                    } else {
                        previousData = data
                    }
                }
                previousData = data
                weatherUIFlow.emit(data)
                Log.e("flow", "emited data $data")
                delay(3_000)
            }
        }
    }

    fun onWeatherRefresh() {
        launch {
            if (locationLat == "no_location") {
                data = CurrentWeatherData(
                    name = "No_location",
                    temp = "-",
                    speed = "-",
                    humidity = "-",
                    description = "no_loc"
                )
            } else if (city != null && cityUpdate) {
                if (isInternetAvailable) {
                    data = getCurrentWeatherCity(city!!)
                } else {
                    previousData = data
                }
            } else if (lat != null && lon != null && locUpdate) {
                if (isInternetAvailable) {
                    data = getCurrentWeather(lat!!, lon!!)
                } else {
                    previousData = data
                }
            } else {
                if (isInternetAvailable) {
                    data = getCurrentWeather(locationLat!!, locationLon!!)
                } else {
                    previousData = data
                }
            }
            previousData = data
            weatherUIFlow.emit(data)
            Log.e("flow", "emited data $data")
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
            name = no_data,
            temp = no_data,
            humidity = no_data,
            description = no_data,
            speed = no_data
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
            name = no_data,
            temp = no_data,
            humidity = no_data,
            description = no_data,
            speed = no_data
        )
    }
}