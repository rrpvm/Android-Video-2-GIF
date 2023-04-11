package com.rrpvm.gif_loader.data.provider.cache_manager

import android.content.Context
import com.rrpvm.gif_loader.common.DefaultCacheManager
import com.rrpvm.gif_loader.data.factory.cache_selector.ICacheSelectorFactoryProvider
import com.rrpvm.gif_loader.domain.entity.ICacheToolManager


class LimitedCacheToolManagerProvider(
    private val cacheSizeCapacity: Int,
    private val cacheSelectorFactory: ICacheSelectorFactoryProvider,
) : ICacheToolManagerProvider {
    override fun provideManager(context: Context): ICacheToolManager {
        return DefaultCacheManager(context, cacheSizeCapacity, cacheSelectorFactory.buildObject())
    }
}