package com.rrpvm.gif_loader.domain.entity

interface IGifDataSource {
    @kotlin.jvm.Throws(Exception::class)
    fun getVideoSource(cacheStrategy: GifLoaderRequestCacheStrategy): ByteArray?
}