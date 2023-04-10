package com.rrpvm.gif_loader

import android.content.Context
import com.rrpvm.gif_loader.common.DEFAULT_CACHE_SIZE_CAPACITY
import com.rrpvm.gif_loader.common.DefaultCacheManager
import com.rrpvm.gif_loader.common.GifDataSourceBuilder
import com.rrpvm.gif_loader.common.GifRequestManager
import com.rrpvm.gif_loader.data.repository.GifCacheRoomRepositoryImpl
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import com.rrpvm.gif_loader.provider.RoomProvider


class GifLoader private constructor(
    private val applicationContext: Context,
    private val cacheSize: Int,
) {
    private val gifCacheRepository: IGifCacheRepository =
        GifCacheRoomRepositoryImpl(
            RoomProvider.provideCacheDao(applicationContext),
            applicationContext
        ).also {
            it.addCacheToolManager(DefaultCacheManager(applicationContext, cacheSize))
        }

    fun withContext(context: Context): GifDataSourceBuilder {
        return GifDataSourceBuilder(
            context,
            GifCacheRoomRepositoryImpl(RoomProvider.provideCacheDao(context), applicationContext),
            workManager = jobManager
        )
    }

    fun loadGif(): GifDataSourceBuilder {
        return GifDataSourceBuilder(
            applicationContext,
            gifCacheRepository,
            workManager = jobManager
        )
    }

    companion object {
        @Volatile
        private var gifLoader: GifLoader? = null
        private val jobManager = GifRequestManager()
        fun get(): GifLoader {
            synchronized(this) {
                return gifLoader ?: throw RuntimeException("Initialize GifLoader before using")
            }
        }

        //for cache management
        fun init(applicationContext: Context, cacheSize: Int = DEFAULT_CACHE_SIZE_CAPACITY) {
            gifLoader = GifLoader(applicationContext, cacheSize)
        }
    }
}