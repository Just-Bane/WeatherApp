package com.example.composeweatherapp.common.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<VS, E, I>(): ViewModel() {

    abstract val defaultState: VS

    @Suppress("LeakingThis")
    protected var _viewState: MutableStateFlow<VS> = MutableStateFlow(defaultState)
    var viewState: StateFlow<VS> = _viewState

    protected var _event: MutableStateFlow<E?> = MutableStateFlow(null)
    var event: StateFlow<E?> = _event

    abstract fun proceedIntent(intent: I)

}