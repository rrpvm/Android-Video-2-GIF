package com.rrpvm.gif_loader.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedResourceState<T>(
    val state: T? = null
)

abstract class SharedResourceSubscriber<T> {
    abstract fun onResourceSuccess(data: T)
    abstract fun onResourceFailed()
}

class SharedResource<T>(
    var state: SharedResourceState<T>? = null
) {
    private val observers = mutableListOf<SharedResourceSubscriber<T>>()
    suspend fun submit(state: SharedResourceState<T>) {
        this.state = state
        observers.forEach {
            withContext(Dispatchers.Main) {
                if (state.state == null) {
                    it.onResourceFailed()
                } else {
                    it.onResourceSuccess(state.state)
                }
            }
        }
    }

    fun addObserver(shrs: SharedResourceSubscriber<T>) {
        observers.add(shrs)
    }

    fun clear() {
        observers.clear()
        state = null
    }
}