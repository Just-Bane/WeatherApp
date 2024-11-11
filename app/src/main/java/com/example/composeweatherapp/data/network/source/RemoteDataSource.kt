package com.dotcode.marble.data.network.source

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val gson: Gson,
): RemoteDataSourceHelper {

    override fun <Api> buildApi(
        api: Class<Api>,
        url: String,
        isCache: Boolean,
    ): Api {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}