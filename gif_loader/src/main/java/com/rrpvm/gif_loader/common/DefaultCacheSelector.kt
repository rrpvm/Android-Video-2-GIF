package com.rrpvm.gif_loader.common

import com.rrpvm.gif_loader.domain.entity.ICacheSelector
import com.rrpvm.gif_loader.domain.model.GifCacheDescription
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository

class DefaultCacheSelector : ICacheSelector {
    override fun getCacheForDelete(repo: IGifCacheRepository): GifCacheDescription? {
      return  repo.getAllCache().firstOrNull()
    }
}