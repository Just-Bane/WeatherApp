package com.example.composeweatherapp.usecase

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import javax.inject.Inject

class DateUseCase @Inject constructor() {

    @SuppressLint("SimpleDateFormat")
    fun getDate(): SimpleDateFormat {
        return SimpleDateFormat("dd.MM")
    }
}