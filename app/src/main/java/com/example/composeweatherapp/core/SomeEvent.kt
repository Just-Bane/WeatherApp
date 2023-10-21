package com.example.composeweatherapp.core

class SomeEvent<V>(private val value: V) {
    fun get(): V {
        return value
    }
}