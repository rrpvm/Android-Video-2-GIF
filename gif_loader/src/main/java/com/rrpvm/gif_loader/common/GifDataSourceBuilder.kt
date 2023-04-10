package com.rrpvm.gif_loader.common

import android.content.Context
import com.rrpvm.gif_loader.domain.entity.IGifDataSource
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import java.net.URL


class GifDataSourceBuilder(
    private val context: Context,
    private val repository: IGifCacheRepository,
    private val workManager: GifRequestManager,
) {
    private class HttpDataSource(val url: String) : IGifDataSource {
        override fun getVideoSource(): ByteArray? {
            val data = kotlin.runCatching {
                return@runCatching URL(url).openStream().use { input ->
                    return@use input.readBytes()
                }
            }
            return data.getOrNull()
        }
    }

    fun from(url: String): GifRequestBuilder {
        return GifRequestBuilder(
            context,
            videoGifSource = url,
            HttpDataSource(url),
            cacheRepository = repository,
            workManager = workManager
        )
    }
}