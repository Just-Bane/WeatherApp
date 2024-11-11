package com.example.composeweatherapp.common.core

class SomeEvent<V>(private val value: V) {
    fun get(): V {
        return value
    }
}