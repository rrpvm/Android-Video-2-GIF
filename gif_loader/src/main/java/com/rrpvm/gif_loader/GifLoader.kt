package com.rrpvm.gif_loader

import android.content.Context
import com.rrpvm.gif_loader.data.repository.GifCacheRoomRepositoryImpl
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import com.rrpvm.gif_loader.provider.RoomProvider


class GifLoader private constructor(private val applicationContext: Context) {
    private val gifCacheRepository: IGifCacheRepository =
        GifCacheRoomRepositoryImpl(
            RoomProvider.provideCacheDao(applicationContext),
            applicationContext
        )

    fun withContext(context: Context): GifResourceLoader {
        return GifResourceLoader(
            context,
            GifCacheRoomRepositoryImpl(RoomProvider.provideCacheDao(context), applicationContext)
        )
    }

    fun loadGif(): GifResourceLoader {
        return GifResourceLoader(applicationContext, gifCacheRepository)
    }

    companion object {
        @Volatile
        private var gifLoader: GifLoader? = null
        fun get(): GifLoader {
            synchronized(this) {
                return gifLoader ?: throw RuntimeException("Initialize GifLoader before using")
            }
        }

        //for cache management
        fun init(applicationContext: Context) {
            gifLoader = GifLoader(applicationContext)
        }
    }
}