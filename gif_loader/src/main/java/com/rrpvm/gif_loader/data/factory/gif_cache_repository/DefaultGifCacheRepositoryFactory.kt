package com.rrpvm.gif_loader.data.factory.gif_cache_repository

import android.content.Context
import com.rrpvm.gif_loader.data.repository.GifCacheRoomRepositoryImpl
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import com.rrpvm.gif_loader.provider.RoomProvider


class DefaultGifCacheRepositoryFactory(
    private val type: IGifCacheRepositoryFactoryType
) : IGifCacheRepositoryFactory {
    override fun provideRepository(
        context: Context
    ): IGifCacheRepository {
        return when (type) {
            IGifCacheRepositoryFactoryType.CacheRepositoryType.ROOM -> {
                GifCacheRoomRepositoryImpl(RoomProvider.provideCacheDao(context), context)
            }
            else -> throw RuntimeException("unknown type")
        }
    }
}