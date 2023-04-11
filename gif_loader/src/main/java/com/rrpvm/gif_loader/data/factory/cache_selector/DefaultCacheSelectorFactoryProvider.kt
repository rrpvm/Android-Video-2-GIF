package com.rrpvm.gif_loader.data.factory.cache_selector

import com.rrpvm.gif_loader.domain.entity.ICacheSelector

class DefaultCacheSelectorFactoryProvider(private val type: CacheSelectorFactory.CacheSelectorFactoryType) :
    ICacheSelectorFactoryProvider {
    private val defaultCacheSelectorFactory: ICacheSelectorFactory = CacheSelectorFactory()

    override fun buildObject(): ICacheSelector {
        return defaultCacheSelectorFactory.provideObject(type)
    }
}