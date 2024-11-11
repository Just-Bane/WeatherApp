package com.example.composeweatherapp.domain.usecase

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class DateUseCase @Inject constructor() {

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        return DateFormat.getDateInstance(DateFormat.LONG).format(Date())
    }
}