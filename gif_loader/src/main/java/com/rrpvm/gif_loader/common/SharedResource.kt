package com.rrpvm.gif_loader.common

import com.rrpvm.gif_loader.domain.entity.SharedResourceState
import com.rrpvm.gif_loader.domain.entity.SharedResourceSubscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedResource<T>(
) {
    var state: SharedResourceState<T>? = null
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