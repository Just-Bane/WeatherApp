package com.example.composeweatherapp.domain.usecase

import retrofit2.Response

abstract class BaseUseCase<in Params, Type> where Type : Any {
    suspend fun getExecute(
        params: Params
    ): Response<Type> {
        return getSuspend(params)
    }

    abstract suspend fun getSuspend(params: Params): Response<Type>
}