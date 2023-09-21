package com.example.composeweatherapp.core

import kotlin.math.pow

fun Double.roundTo(number: Int): Double {
    return (Math.round(this * 10.0.pow(number)))/10.0.pow(number)
}