package com.rrpvm.gif_loader.domain.entity

import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository


interface ICacheToolManager {
    fun onAddCache(repo : IGifCacheRepository, cacheSize : Int)
    fun onInit(repo : IGifCacheRepository)

    fun onReset()
}