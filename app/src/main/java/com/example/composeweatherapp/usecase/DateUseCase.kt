package com.example.composeweatherapp.usecase

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.inject.Inject

class DateUseCase @Inject constructor() {

    @SuppressLint("SimpleDateFormat")
    fun getDate(): DateFormat {
        return DateFormat.getDateInstance(DateFormat.LONG)
    }
}