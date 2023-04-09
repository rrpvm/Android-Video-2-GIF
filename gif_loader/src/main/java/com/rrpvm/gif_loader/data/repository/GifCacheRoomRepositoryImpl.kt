package com.rrpvm.gif_loader.data.repository

import android.content.Context
import com.rrpvm.gif_loader.data.dao.CacheDbRoomDao
import com.rrpvm.gif_loader.data.model.GifCacheModelRoom
import com.rrpvm.gif_loader.data.room.CACHE_DB_VERSION
import com.rrpvm.gif_loader.domain.model.GifModel
import com.rrpvm.gif_loader.domain.model.GifParameters
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import java.io.File

class GifCacheRoomRepositoryImpl(
    private val roomDao: CacheDbRoomDao,
    private val context: Context,
) : IGifCacheRepository {
    override fun putCache(cache: GifModel) {
        try {
            val gifOutput =
                File(context.cacheDir, "${cache.mOriginSource.hashCode()}__${cache.hashCode()}.gif")
            gifOutput.outputStream().use {
                it.write(cache.mGifData)
            }
            val gifSourcePath = gifOutput.path
            roomDao.createGifCache(
                GifCacheModelRoom(
                    mName = cache.mOriginSource,
                    mSourcePath = gifSourcePath,
                    mSize = gifOutput.totalSpace,
                    mVersion = CACHE_DB_VERSION,
                    mParamHashcode = cache.mParamHashcode,
                    mCreatedAt = cache.mCreatedAt
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getCache(name: String, parameters: GifParameters): GifModel? {
        return roomDao.getGifCache(name, parameters.hashCode())?.toDomain()
    }

    override fun deleteCache(name: String) {
        throw RuntimeException("not already implemented")
    }
}