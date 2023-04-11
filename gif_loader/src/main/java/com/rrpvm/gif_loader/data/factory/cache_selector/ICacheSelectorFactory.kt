package com.rrpvm.gif_loader.data.factory.cache_selector

import com.rrpvm.gif_loader.domain.entity.ICacheSelector

interface  ICacheSelectorFactory {
    fun provideObject(type: ICacheSelectorFactoryType) : ICacheSelector
    interface ICacheSelectorFactoryType
}