package com.rrpvm.gif_loader.data.factory.cache_selector

import com.rrpvm.gif_loader.domain.entity.ICacheSelector

interface ICacheSelectorFactoryProvider {
    fun buildObject(): ICacheSelector
}

val DEFAULT_CACHE_SELECTOR_FACTORY_PROVIDER =
    DefaultCacheSelectorFactoryProvider(CacheSelectorFactory.CacheSelectorFactoryType.LAST_DATE_CACHE)