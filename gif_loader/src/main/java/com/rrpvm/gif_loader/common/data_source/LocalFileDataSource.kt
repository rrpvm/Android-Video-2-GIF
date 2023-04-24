package com.rrpvm.gif_loader.common.data_source

import com.rrpvm.gif_loader.domain.entity.GifLoaderRequestCacheStrategy
import com.rrpvm.gif_loader.domain.entity.IGifDataSource
import java.io.File
import java.io.IOException

class LocalFileDataSource(private val path: String) : IGifDataSource {
    @kotlin.jvm.Throws(IOException::class)
    override fun getVideoSource(cacheStrategy: GifLoaderRequestCacheStrategy): ByteArray {
        return File(path).inputStream().use { input ->
            return@use input.readBytes()
        }
    }
}
