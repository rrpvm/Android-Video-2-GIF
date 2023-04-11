package com.rrpvm.gif_loader.data.factory.cache_selector

import com.rrpvm.gif_loader.common.DefaultCacheSelector
import com.rrpvm.gif_loader.domain.entity.ICacheSelector

class CacheSelectorFactory : ICacheSelectorFactory {
    override fun provideObject(type: ICacheSelectorFactory.ICacheSelectorFactoryType): ICacheSelector {
        return when (type) {
            CacheSelectorFactoryType.LAST_DATE_CACHE -> {
                DefaultCacheSelector()
            }
            else -> throw RuntimeException("unknown type for CacheSelectorFactory")
        }
    }

    enum class CacheSelectorFactoryType : ICacheSelectorFactory.ICacheSelectorFactoryType {
        LAST_DATE_CACHE
    }
}
