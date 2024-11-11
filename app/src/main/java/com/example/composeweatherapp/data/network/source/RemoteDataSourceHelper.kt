package com.dotcode.marble.data.network.source

interface RemoteDataSourceHelper {
    fun <Api> buildApi(api: Class<Api>, url: String, isCache: Boolean = false): Api
}