package com.rrpvm.gif_loader.common

import android.content.Context
import com.rrpvm.gif_loader.common.data_source.HttpDataSource
import com.rrpvm.gif_loader.common.data_source.LocalFileDataSource
import com.rrpvm.gif_loader.domain.entity.IGifDataSource
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import java.net.URL


class GifDataSourceBuilder(
    private val context: Context,
    private val repository: IGifCacheRepository,
    private val workManager: GifRequestManager,
) {
    fun from(url: String): GifRequestBuilder {
        return GifRequestBuilder(
            context,
            videoGifSource = url,
            HttpDataSource(url),
            cacheRepository = repository,
            workManager = workManager
        )
    }

    fun fromLocal(localPath: String): GifRequestBuilder {
        return GifRequestBuilder(
            context,
            videoGifSource = localPath,
            LocalFileDataSource(localPath, context),
            cacheRepository = repository,
            workManager = workManager
        )
    }
}