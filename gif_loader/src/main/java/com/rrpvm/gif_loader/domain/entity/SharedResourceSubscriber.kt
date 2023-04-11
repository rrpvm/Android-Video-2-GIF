package com.rrpvm.gif_loader.domain.entity

abstract class SharedResourceSubscriber<T> {
    abstract fun onResourceSuccess(data: T)
    abstract fun onResourceFailed()
}