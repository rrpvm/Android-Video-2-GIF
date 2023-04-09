package com.rrpvm.gif_loader.domain.repository

import com.rrpvm.gif_loader.domain.model.GifModel
import com.rrpvm.gif_loader.domain.model.GifParameters

interface IGifCacheRepository {
    fun putCache(cache: GifModel)
    fun getCache(name: String, parameters: GifParameters): GifModel?
    fun deleteCache(name: String)
}