package com.example.composeweatherapp.ui.screens.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeweatherapp.usecase.InternetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val internetUC: InternetUseCase
): ViewModel() {

    var networkStatus = mutableStateOf("")

    init {
        viewModelScope.launch {
            internetUC.observe().collect {
                withContext(Dispatchers.Main) {
                    networkStatus.value = it.name
                }
            }
        }
    }

    fun isOnline(): Boolean { return internetUC.isOnline() }
}