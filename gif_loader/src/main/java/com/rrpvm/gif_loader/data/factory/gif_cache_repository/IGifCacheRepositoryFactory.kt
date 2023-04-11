package com.rrpvm.gif_loader.data.factory.gif_cache_repository

import android.content.Context
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository

interface IGifCacheRepositoryFactory {
    fun provideRepository(
        context: Context,
    ): IGifCacheRepository
}

