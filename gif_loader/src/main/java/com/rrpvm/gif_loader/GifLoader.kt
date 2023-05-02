package com.rrpvm.gif_loader

import android.content.Context
import com.rrpvm.gif_loader.common.*
import com.rrpvm.gif_loader.data.provider.cache_manager.ICacheToolManagerProvider
import com.rrpvm.gif_loader.data.provider.cache_manager.LimitedCacheToolManagerProvider
import com.rrpvm.gif_loader.data.factory.cache_selector.DEFAULT_CACHE_SELECTOR_FACTORY_PROVIDER
import com.rrpvm.gif_loader.data.factory.gif_cache_repository.DefaultGifCacheRepositoryFactory
import com.rrpvm.gif_loader.data.factory.gif_cache_repository.IGifCacheRepositoryFactory
import com.rrpvm.gif_loader.data.factory.gif_cache_repository.IGifCacheRepositoryFactoryType
import kotlinx.coroutines.Job

const val SV_MAX_FILE_SIZE = 1024 * 1024 * 64//64MB

class GifLoader private constructor() {
    companion object {
        private val jobManager = GifRequestManager()
        private var cToolManagerFactory: ICacheToolManagerProvider =
            LimitedCacheToolManagerProvider(
                DEFAULT_CACHE_SIZE_CAPACITY, DEFAULT_CACHE_SELECTOR_FACTORY_PROVIDER
            )
        private var cRepositoryFactory: IGifCacheRepositoryFactory =
            DefaultGifCacheRepositoryFactory(IGifCacheRepositoryFactoryType.CacheRepositoryType.ROOM)

        fun setCacheManagerToolFactory(factory: ICacheToolManagerProvider) {
            this.cToolManagerFactory = factory
        }

        fun setRepositoryMode(factory: IGifCacheRepositoryFactory) {
            cRepositoryFactory = factory
        }

        fun withContext(context: Context): GifDataSourceBuilder {
            return GifDataSourceBuilder(
                context,
                cRepositoryFactory.provideRepository(context).also {
                    it.addCacheToolManager(
                        synchronized(GifLoader) {
                            cToolManagerFactory.provideManager(context)
                        }
                    )
                }, jobManager
            )
        }

        fun clearTask(job: Job) {
            jobManager.deleteJob(job)
        }
    }
}