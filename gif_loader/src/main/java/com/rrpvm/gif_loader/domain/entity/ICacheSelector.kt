package com.rrpvm.gif_loader.domain.entity

import com.rrpvm.gif_loader.domain.model.GifCacheDescription
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository

interface ICacheSelector {
    fun getCacheForDelete(repo : IGifCacheRepository) : GifCacheDescription?
}