package com.rrpvm.gif_loader.domain.repository

import com.rrpvm.gif_loader.domain.entity.ICacheToolManager
import com.rrpvm.gif_loader.domain.model.GifCacheDescription
import com.rrpvm.gif_loader.domain.model.GifModel
import com.rrpvm.gif_loader.domain.model.GifParameters

interface IGifCacheRepository {
    fun putCache(cache: GifModel)
    fun getCache(name: String, parameters: GifParameters): GifModel?
    fun getAllCache(): List<GifCacheDescription>
    fun deleteCache(name: String)
    fun removeCache(cacheList: List<GifCacheDescription>)
    fun getCacheList(page: Int, limit: Int): List<GifCacheDescription>
    fun addCacheToolManager(toolManager: ICacheToolManager)

    fun clearRoomSqlStatement()
}